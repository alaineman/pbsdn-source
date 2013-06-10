package sdnscripts.pestcontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import javax.swing.JOptionPane;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.Widget;

@Manifest(authors = {"Ariane"}, name = "InControl", description = "Plays the pest control minigame", version = 0.1)
public class PestMain extends ActiveScript {

    public static Island game;
    /**
     * A queue of the rewards to get.
     */
    static Queue<Reward> rewardQueue;
    /**
     * The chosen difficulty to play.
     */
    static int difficultySelection = -1;
    //Timer for dynamic sleeps
    public static Timer t = new Timer(5000);
    //Whether or not to go exchange.
    public static boolean spendTickets = false;
    public static final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
    public static Tree jobContainer = null;

    public static synchronized void provide(final Node... jobs) {
        for (final Node job : jobs) {
            if (!jobsCollection.contains(job)) {
                jobsCollection.add(job);
            }
        }
        jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }

    @Override
    public void onStart() {
        game = new Island();
    }   

    @Override
    public int loop() {
        if (jobContainer != null) {
            final Node job = jobContainer.state();
            if (job != null) {
                if (!PestMain.rewardQueue.isEmpty()) {
                    onStop();
                }
                jobContainer.set(job);
                getContainer().submit(job);
                job.join();
            }
        }
        return Random.nextInt(400, 700);
    }

    @Override
    public void onStop() {
        JOptionPane.showMessageDialog(null, "Thank you for using InControl.");
        shutdown();
    }
    

    class Board extends Node {

        //Widget in the boat
        Widget infoWidget = Widgets.get(407);

        @Override
        public boolean activate() {
            return !game.isInGame() && !infoWidget.validate() && !spendTickets;
        }

        @Override
        public void execute() {
            int[] plankIds = {14315, 25631, 25632};
            if (difficultySelection != -1) {
                SceneObject plank = SceneEntities.getNearest(plankIds[difficultySelection]);
                if (plank != null) {
                    if (!plank.isOnScreen()) {
                        Walking.walk(plank);
                        return;
                    }
                    plank.interact("Cross");
                    t.reset();
                    while (t.isRunning() && !infoWidget.validate()) {
                        Task.sleep(200, 400);
                    }
                    if (infoWidget.validate()) {
                        String pointText = infoWidget.getChild(16).getText();
                        String[] textElements = pointText.split(":");
                        if (textElements != null && textElements.length > 1) {
                            int pointAmount = Integer.parseInt(textElements[1].trim());
                            if (pointAmount > 450) {
                                spendTickets = true;
                            }
                        }
                    }
                }
                switch (difficultySelection) {
                    case 0:
                        Walking.walk(new Tile(2657, 2639, 0));
                        break;
                    case 1:
                        Walking.walk(new Tile(2644, 2644, 0));
                        break;
                    case 2:
                        Walking.walk(new Tile(2638, 2653, 0));
                        break;
                }
                t.reset();
                while (t.isRunning() && Calculations.distanceTo(Walking.getDestination()) > 4) {
                    Task.sleep(300, 500);
                }
            }
        }
    }   
}
