package sdnscripts.namehunt;

import org.powerbot.core.Bot;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 *
 * @author Vincent W
 */
@Manifest(authors = {"Alaineman"}, name = "NameHunter", description = "Snipes names", version = 0.1)
public class NameHunter extends ActiveScript {

    private int accountCreationWidget = 1028;
    private int contWidget = 139;
    private int textField = 190;
    private int nameContinue = 182;
    private Tree jobs = null;
    private String name = "the_name_you_want_to_snipe"; //Enter the name you want to snipe
    
    @Override
    public void onStart() {
        System.out.print("pannenkoek");
    }

    @Override
    public int loop() {
        if (jobs == null) {
            jobs = new Tree(new Node[]{new Snipe()});
        }
        final Node job = jobs.state();
        if (job != null) {
            jobs.set(job);
            getContainer().submit(job);
            job.join();
            return 0;
        }
        return Random.nextInt(400, 500);
    }
    
    public class Snipe extends Node {
        private Widget acw = Widgets.get(accountCreationWidget);
        
        @Override
        public boolean activate() { 
            
            return Game.isLoggedIn() && (Game.getClientState() != Game.INDEX_LOBBY_SCREEN) && acw.validate();
        }

        @Override
        public void execute() {
            WidgetChild step1 = Widgets.get(accountCreationWidget, contWidget);
            if(step1 != null && step1.validate()){
                step1.click(true);
                Task.sleep(1000,2000);
                step1.click(true);
            }           
            WidgetChild nameBox = Widgets.get(accountCreationWidget, textField);
            if(nameBox != null && nameBox.validate()){
                nameBox.click(true);
                Task.sleep(300,500);
                Keyboard.sendText(name, true);
            }
            
            /*
            WidgetChild continueBox = Widgets.get(accountCreationWidget, nameContinue);
            if(continueBox != null && continueBox.validate()){
                continueBox.click(true);
                Task.sleep(350, 500);
            }            */
            
            if(!acw.validate()){
                log.warning("This account has been named");
                Bot.instance().getScriptHandler().stop();
            }
        }        
    }
}
