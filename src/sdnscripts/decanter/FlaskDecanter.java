package sdnscripts.decanter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/* 
 * 
 * @author Alaineman
 */
@Manifest(authors = {"Alaineman"}, name = "Flask Decanter", description = "Time for a 6 pack.", version = 0.1, vip = false, hidden = false)
public class FlaskDecanter extends ActiveScript {

    /**
     * A List of Node elements which will push to the loop each time.
     */
    public static final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
    /**
     * A tree structure in which the Node's are processed in the loop.
     */
    public static Tree jobContainer = null;
    private int overload4 = 15332;           
    private int emptyFlask = 23191;
    private Timer run = new Timer(4000);

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
        provide(new Banking(), new Convert());
    }

    @Override
    public int loop() {
        if (jobContainer != null) {
            final Node job = jobContainer.state();
            if (job != null) {
                jobContainer.set(job);
                getContainer().submit(job);
                job.join();
            }
        }
        return Random.nextInt(400, 700);
    }

    private class Convert extends Node {

        @Override
        public boolean activate() {
            return !Bank.isOpen();
        }

        @Override
        public void execute() {
            if (Inventory.getCount(emptyFlask) == 0) {
                Bank.open();
                run.reset();
                while (run.isRunning() && !Bank.isOpen()) {
                    Task.sleep(300, 500);
                }
            } else {
                if (!Widgets.get(1251).validate()) {
                    if (Inventory.getSelectedItem() == null || Inventory.getSelectedItem().getId() == overload4) {
                        Inventory.selectItem(overload4);
                    }
                    run.reset();
                    while (run.isRunning() && !Inventory.isItemSelected()) {
                        Task.sleep(300, 500);
                    }
                    Inventory.getItem(emptyFlask).getWidgetChild().click(true);                    
                    run.reset();
                    while (run.isRunning() && !Widgets.get(1188).validate()) {
                        Task.sleep(300, 500);
                    }
                    WidgetChild soak = Widgets.get(1188, 29);                    
                    soak.click(true);
                    run.reset();
                    while (run.isRunning() && Inventory.contains(emptyFlask)) {
                        Task.sleep(300, 500);
                    }
                }
            }
        }
    }

    private class Banking extends Node {

        @Override
        public boolean activate() {
            return Bank.isOpen();
        }

        @Override
        public void execute() {
            if (Bank.getItem(overload4).getStackSize()<20) {
                stop();
            } else {
                if (Inventory.contains(emptyFlask)) {
                    Bank.close();
                } else {
                    Bank.depositInventory();
                    while (run.isRunning() && Inventory.getCount() > 0) {
                        Task.sleep(300, 500);
                    }
                    Bank.withdraw(emptyFlask, 11);
                    run.reset();
                    while (run.isRunning() && Inventory.getCount() == 0) {
                        Task.sleep(300, 500);
                    }
                    Bank.withdraw(overload4, Bank.Amount.ALL);
                    run.reset();
                    while (run.isRunning() && !Inventory.isFull()) {
                        Task.sleep(300, 500);
                    }
                }
            }
        }
    }
}
