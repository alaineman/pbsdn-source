package sdnscripts.pyrelogging;

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
@Manifest(authors = {"Alaineman"}, name = "Pyre Log Maker", description = "Pyre them logs.", version = 0.1, topic = 1000454, vip = false, hidden = false)
public class pyreLogMaker extends ActiveScript {

    /**
     * A List of Node elements which will push to the loop each time.
     */
    public static final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
    /**
     * A tree structure in which the Node's are processed in the loop.
     */
    public static Tree jobContainer = null;
    private int sacredOil4 = 3430;
    private int emptyVial = 229;
    private int[] logs = {1517};
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
            if (Inventory.getCount(emptyVial) == 14 || !Inventory.isFull()) {
                Bank.open();
                run.reset();
                while (run.isRunning() && !Bank.isOpen()) {
                    Task.sleep(300, 500);
                }
            } else {
                if (!Widgets.get(1251).validate()) {
                    if (Inventory.getSelectedItem() == null || Inventory.getSelectedItem().getId() == sacredOil4) {
                        Inventory.selectItem(sacredOil4);
                    }
                    run.reset();
                    while (run.isRunning() && !Inventory.isItemSelected()) {
                        Task.sleep(300, 500);
                    }
                    Inventory.getItem(logs).getWidgetChild().click(true);
                    run.reset();
                    WidgetChild soak = Widgets.get(1370, 38);
                    while (run.isRunning() && !Widgets.get(1370, 38).validate()) {
                        Task.sleep(300, 500);
                    }
                    soak.click(true);
                    run.reset();
                    while (run.isRunning() && !Widgets.get(1251).validate()) {
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
            if (Bank.getItemCount(sacredOil4) <= 0 || Bank.getItemCount(logs[0]) <= 0) {
                stop();
            } else {
                if (Inventory.contains(sacredOil4)) {
                    Bank.close();
                } else {
                    Bank.depositInventory();
                    while (run.isRunning() && Inventory.getCount() > 0) {
                        Task.sleep(300, 500);
                    }
                    Bank.withdraw(logs[0], 14);
                    run.reset();
                    while (run.isRunning() && Inventory.getCount() == 0) {
                        Task.sleep(300, 500);
                    }
                    Bank.withdraw(sacredOil4, Bank.Amount.ALL);
                    run.reset();
                    while (run.isRunning() && !Inventory.isFull()) {
                        Task.sleep(300, 500);
                    }
                }
            }
        }
    }
}
