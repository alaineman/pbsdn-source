package sdnscripts.scaffoldbuilder;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 *
 * @author Vincent W
 */
@Manifest(authors = {"Alaineman"}, name = "ScaffoldBuilder", description = "Build Scaffolds", version = 0.71, topic = 1000454, vip = false, hidden = false)
public class ScaffoldBuilder extends ActiveScript {

    private final int swn = 1386; //scaffold widget number    
    private final int[] DONE_ID = {7, 12, 17, 24, 29};
    private final int[] COUNT_ID = {117, 109, 101, 93, 85};
    public static JFrame gui;
    public static boolean guiRunning;
    public static ArrayList<BuildingSite> sites = new ArrayList<>(5);
    private int GREEN = 63488;
    private Tree jobs = null;

    @Override
    public void onStart() {

        guiRunning = true;
        sites = new ArrayList<>(5);
        gui = new SBGUI("Scaffold Builder GUI");
        gui.setVisible(true);
    }

    @Override
    public int loop() {
        if (guiRunning) {
            log.info("Gui is still running");
            return 800;
        }
        if (jobs == null) {
            jobs = new Tree(new Node[]{new BuildMode(), new Control()});
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

    @Override
    public void onStop() {
        JOptionPane.showMessageDialog(null, "Thank you for using Scaffold Builder.");
        stop();
    }

    public static void addSite(BuildingSite bs) {
        sites.add(bs);
        JOptionPane.showMessageDialog(null, "Building site " + bs.getName() + " has been added");
        JOptionPane.showMessageDialog(null, "You now have a total of " + sites.size() + " added.");
    }

    private class Control extends Node {

        private final String[] XP_OPTION = {"Prayer", "Slayer"};
        private final String BUILD_ACTION = "Build scaffold";

        @Override
        public boolean activate() {
            Widget scaffoldWidget = Widgets.get(swn);
            return !scaffoldWidget.validate() && sites != null;
        }

        @Override
        public void execute() {
            if (sites.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All building sites have been completed.");
                onStop();
            }
            BuildingSite current = sites.get(0);
            if (current == null) {
                JOptionPane.showMessageDialog(null, "First element is null.");
                return;
            }
            NPC copernicus = NPCs.getNearest(current.getBuilder());
            //Checks whether or not we are in buildmodus.
            if (copernicus != null && copernicus.isOnScreen()) {
                Widget optionWidget = Widgets.get(1188);
                //Introduce time-out to prevent infinite while loops.
                Timer wait = new Timer(5000);
                //If the option menu is not visible, interact with copernicus.               
                if (!optionWidget.validate()) {
                    copernicus.interact(BUILD_ACTION);
                    while (wait.isRunning() && !optionWidget.validate()) {
                        Task.sleep(100, 200);
                    }
                } else {
                    //Select the type of statue we want to build (which xp we gain)
                    String option = current.getReward() ? XP_OPTION[0] : XP_OPTION[1];
                    for (WidgetChild i : optionWidget.getChildren()) {
                        if (i.getText().contains(option)) {
                            i.click(true);
                            break;
                        }
                    }
                    wait.reset();
                    WidgetChild continueButton = Widgets.get(1184, 18); //18-21    
                    while (wait.isRunning() && !continueButton.visible()) {
                        Task.sleep(500, 800);
                    }
                    wait.reset();
                    if (continueButton == null) {
                        return;
                    }
                    //Dealing with the continue buttons
                    for (int i = 0; i < 3; i++) {
                        continueButton.click(true);
                        Task.sleep(700, 1300);
                    }
                }
            } else {
                openLodeStones();
                if (current.getName().equalsIgnoreCase("taverley")) {
                    WidgetChild tav = Widgets.get(1092, 50);
                    tav.click(true);
                    Timer longRun = new Timer(30000);
                    while (Players.getLocal().getAnimation() != 16393 && longRun.isRunning()) {
                        Task.sleep(300, 500);
                    }
                    longRun.reset();
                    while (Players.getLocal().getAnimation() == 16393 && longRun.isRunning()) {
                        Task.sleep(300, 500);
                    }
                } else if (current.getName().equalsIgnoreCase("lumbridge")) {
                    WidgetChild lumb = Widgets.get(1092, 47);
                    lumb.click(true);
                    Timer longRun = new Timer(30000);
                    while (Players.getLocal().getAnimation() != 16393 && longRun.isRunning()) {
                        Task.sleep(300, 500);
                    }
                    longRun.reset();
                    while (Players.getLocal().getAnimation() == 16393 && longRun.isRunning()) {
                        Task.sleep(300, 500);
                    }
                } else if (current.getName().equalsIgnoreCase("canifis")) {
                    WidgetChild lumb = Widgets.get(1092, 62); //close it again
                    lumb.click(true);
                    Timer run = new Timer(7000);
                    while (run.isRunning() && Widgets.get(1092).validate()) {
                        Task.sleep(300, 500);
                    }
                    Item ecto = Inventory.getItem(4251);
                    if (ecto == null) {
                        JOptionPane.showMessageDialog(null, "No ectophial found.");
                        onStop();
                    }
                    ecto.getWidgetChild().click(true);
                    run.reset();
                    while (Players.getLocal().getAnimation() != 832 && run.isRunning()) {
                        Task.sleep(300, 500);
                    }
                    run.reset();
                    while (Players.getLocal().getAnimation() == 832 && run.isRunning()) {
                        Task.sleep(300, 500);
                    }
                    Timer longRun = new Timer(30000);
                    while (Calculations.distanceTo(current.getLocation()) > 5 && longRun.isRunning()) {
                        Walking.walk(current.getLocation());
                    }
                } else if (current.getName().equalsIgnoreCase("yanille")) {
                    WidgetChild yan = Widgets.get(1092, 52);
                    yan.click(true);
                    Timer longRun = new Timer(30000);
                    while (Players.getLocal().getAnimation() != 16393 && longRun.isRunning()) {
                        Task.sleep(300, 500);
                    }
                    longRun.reset();
                    while (Players.getLocal().getAnimation() == 16393 && longRun.isRunning()) {
                        Task.sleep(300, 500);
                    }
                } else {
                    //something went very wrong...
                }
                Timer reachTimer = new Timer(60000);
                while (Calculations.distanceTo(current.getLocation()) > 5 && reachTimer.isRunning()) {
                    Walking.walk(current.getLocation());
                    Task.sleep(4000, 7000);
                }
            }
        }

        public void openLodeStones() {
            if (!Tabs.ABILITY_BOOK.open()) {
                Task.sleep(400, 600);
            }
            Widgets.get(275, 41).click(true);
            Task.sleep(600, 1000);
            Widgets.get(275, 38).click(true);
            Task.sleep(600, 1000);
            Widgets.get(275, 18).getChild(155).click(true);
            Timer wait = new Timer(3500);
            while (!Widgets.get(1092).validate() && wait.isRunning()) {
                Task.sleep(200, 500);
            }
        }
    }

    private class BuildMode extends Node {

        private WidgetChild usedData = Widgets.get(swn, 71);
        //private WidgetChild finished = Widgets.get(swn, 73);
        private WidgetChild highestData = Widgets.get(swn, 75);
        private WidgetChild lowestData = Widgets.get(swn, 77);
        private int used = -1;
        private int totalPieces = -1;
        private int highestTier = -1;
        private int lowestTier = -1;
        private int[] tierCounts = {-1, -1, -1, -1, -1};
        private boolean init = false;

        @Override
        public boolean activate() {
            Widget scaffoldWidget = Widgets.get(swn);
            return scaffoldWidget.validate();
        }

        @Override
        public void execute() {

            //Update the used log and total pieces
            totalPieces = updateData(usedData, true);
            used = updateData(usedData, false);

            //Checks the highest tier needed
            if (highestTier == -1) {
                highestTier = updateData(highestData, true);
            }
            //Check the lowest tier needed
            if (lowestTier == -1) {
                lowestTier = updateData(lowestData, true);
            }

            for (int i = 0; i < tierCounts.length; i++) {
                tierCounts[i] = countCheck(COUNT_ID[i]);
            }
            /*check if it can do stuff
             log.info("Highest tier is " + highestTier);
             log.info("Lowest tier is " + lowestTier);
             log.info("Total amount of pieces is " + totalPieces);
             */
            if (lowestTier != -1 && highestTier != -1 && used != -1 && totalPieces != -1) {
                if (!init) {
                    baseMove();
                    init = !init;
                } else {
                    iteration();
                }
                Task.sleep(600, 1000);
                verify();
                Timer wait = new Timer(4000);
                for (int i = 4; i >= 0; i--) {
                    WidgetChild continueButton = Widgets.get(1184, 18); //18-21 
                    while (wait.isRunning() && !continueButton.visible()) {
                        Task.sleep(300, 400);
                    }
                    continueButton.click(true);
                }
                for (int i = 0; i < 5; i++) {
                    if (!pillarDone(i)) {
                        return;
                    }
                }
                Timer sculputer = new Timer(35000);
                while(sculputer.isRunning() && Players.getLocal().getAnimation() != 19425){
                    Task.sleep(700,800);
                }                
                while(sculputer.isRunning() && !Players.getLocal().isIdle()){
                    Task.sleep(400,700);
                }
                if (sites.get(0).getReward()) {
                    int[] statues = {81950};
                    SceneObject statue = SceneEntities.getNearest(statues);
                    if (statue == null) {
                        return;
                    }
                    statue.click(true);
                    Task.sleep(800, 1500);
                } else {
                    //fight part.
                }
                sites.remove(0);
                if (sites.isEmpty()) {
                    onStop();
                }
            }
        }

        private void baseMove() {
            //First satisfy the lower tier requirement
            for (int i = 0; i < 5; i++) {
                for (int j = 1; j < lowestTier; j++) {
                    pillarUp(i);
                }
                tierCounts[i] = lowestTier;
            }
            used = lowestTier * 5;
            int left = totalPieces - used;
            for (int m = 4; m >= 0; m--) {
                if (left == 0) {
                    break;
                }
                while (left > 0 && tierCounts[m] < highestTier) {
                    pillarUp(m);
                    tierCounts[m]++;
                    used++;
                    left--;
                }
            }
        }

        private void iteration() {
            int left = 0;
            for (int i = 4; i >= 0; i--) {
                if (pillarDone(i)) {
                    continue;
                } else {
                    if (tierCounts[i] > lowestTier) {
                        if (pillarDown(i)) {
                            JOptionPane.showMessageDialog(null, "Tier " + i + "has been decreased by 1.");
                            tierCounts[i]--;
                            left++;
                        }
                        int j = 1;
                        while (left > 0) {
                            if (!pillarDone(i - j) && tierCounts[i - j] < highestTier) {
                                if (pillarUp(i - j)) {
                                    tierCounts[i - j]++;
                                    left--;
                                    return;
                                }
                            } else {
                                j++;
                            }
                        }
                    }
                    break;
                }
            }
        }

        //index true is field 1, false is 0
        private int updateData(WidgetChild wc, boolean index) {
            if (wc.validate()) {
                String status = wc.getText();
                String[] splits = status.split("/");
                if (splits == null) {
                    System.err.println("Used string is NULL");
                    return -1;
                }
                int i = index ? 1 : 0;
                if (splits.length != 2 || splits[0] == null
                        || splits[1] == null) {
                    System.out.println("Used string field index is: " + splits.toString());
                }
                return Integer.parseInt(splits[i].trim());
            }
            return -1;
        }

        private int countCheck(int id) {
            WidgetChild wc = Widgets.get(swn, id);
            if (wc != null && wc.validate()) {
                return Integer.parseInt(wc.getText().trim());
            }
            return -1;
        }

        private boolean pillarDone(int pillar) {
            Widget scaffoldWidget = Widgets.get(swn);
            if (scaffoldWidget.validate()) {
                WidgetChild upButton = Widgets.get(swn, DONE_ID[pillar]);
                return upButton.visible();
            }
            return false;
        }

        private boolean pillarUp(int pillar) {
            Widget scaffoldWidget = Widgets.get(swn);
            if (scaffoldWidget.validate()) {
                WidgetChild upButton = Widgets.get(swn, DONE_ID[pillar] + 2);
                return upButton.click(true);
            }
            JOptionPane.showMessageDialog(null, "Widget UP not found.");
            return false;
        }

        private boolean pillarDown(int pillar) {
            Widget scaffoldWidget = Widgets.get(swn);
            if (scaffoldWidget.validate()) {
                WidgetChild downButton = Widgets.get(swn, DONE_ID[pillar] + 3);
                return downButton.click(true);
            }
            return false;
        }

        private boolean verify() {
            WidgetChild confirmButton = Widgets.get(swn, 68);
            if (conditionsMet() && confirmButton.validate()) {
                return confirmButton.click(true);
            }
            return false;
        }

        private boolean conditionsMet() {
            if (highestData != null && lowestData != null && usedData != null) {
                if (highestData.getTextColor() == GREEN
                        && lowestData.getTextColor() == GREEN
                        && usedData.getTextColor() == GREEN) {
                    return true;
                }
            }
            return false;
        }
    }
}
