package sdnscripts.shadesofmortton;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.Widget;

/**
 * This class represents the Altar bits of the script.
 *
 * @author Alaineman
 */
public class Altar {

    public static Widget templeScoresWidget = Widgets.get(328);
    public static int sanctityCount = -1;
    public static int resourcesCount = 0;
    public static int templeStatus = 100;
    public static int[] shadeId = {1241, 1240};
    public static Tile altar = new Tile(3506, 3315, 0);
    public static Timer runTimer = new Timer(4500);

    public static int countNeededSanctity() {
        int result = 10;
        for (int i = 0; i < 4; i++) {
            result += Inventory.getCount(SacredOil.oliveOils[i]) * (i + 1);
        }
        if (result > 100) {
            return 100;
        }
        return result;
    }

    public static void updateSanctity() {
        if (templeScoresWidget.validate() && templeScoresWidget.getChild(5).getText() != null) {
            sanctityCount = Integer.parseInt(templeScoresWidget.getChild(5).getText());
        }
    }

    public static void convertOil() {
        SceneObject unlit = SceneEntities.getNearest(4091);
        if (unlit != null && unlit.isOnScreen()) {
            unlit.click(true);
            runTimer.reset();
            while (unlit.validate() && runTimer.isRunning()) {
                Task.sleep(300, 500);
            }
        } else {
            Walking.walk(unlit);
        }
        SceneObject fireAltar = SceneEntities.getNearest(4090);
        if (fireAltar != null) {
            if (fireAltar.isOnScreen()) {
                int targetItem = 0;
                for (int i = 0; i < 4; i++) {
                    if (Inventory.getCount(SacredOil.oliveOils[i]) > 0) {
                        targetItem = SacredOil.oliveOils[i];
                        break;
                    }
                }
                if (!Inventory.isItemSelected()) {
                    Inventory.selectItem(targetItem);
                }
                runTimer.reset();
                while (runTimer.isRunning() && !Inventory.isItemSelected()) {
                    Task.sleep(300, 500);
                }
                fireAltar.click(true);
                Timer runOutTimer = new Timer(15000);
                while (runOutTimer.isRunning() && Inventory.getCount(SacredOil.oliveOils) > 0) {
                    Task.sleep(400, 700);
                }
            } else {
                Walking.walk(fireAltar);
            }
        }
    }

    public static void makeSacredOils() {
        updateSanctity();
        if (sanctityCount > countNeededSanctity()) {
            updateTempleStatus();
            if (templeStatus < 100) {
                while (templeStatus < 100) {
                    updateTempleStatus();
                    attackShade();
                }
            }
            convertOil();
        } else {
            if (SacredOil.fightModus) {
                attackShade();
            } else {
                repairTemple();
            }
        }
    }

    public static void repairTemple() {
        int buildAnimation = 8889;
        int repairAnimation = 0;// don't know
        int reinforceAnimation = 8943;

        int[] brokenWalls = {4068, 4079};
        int[] templeWallsRubble = {4069, 4080};//might be more
        int[] templeWallsComplete = {4078, 4089};

        SceneObject brokenWall = SceneEntities.getNearest(brokenWalls);
        SceneObject repairObject = SceneEntities.getNearest(templeWallsRubble);
        SceneObject reinforceObject = SceneEntities.getNearest(templeWallsComplete);
        Timer longTimer = new Timer(10000);

        System.out.println("Repairing the temple");
        if (brokenWall != null) {
            brokenWall.click(true);
            while (Players.getLocal().getAnimation() == buildAnimation && longTimer.isRunning()) {
                Task.sleep(300, 500);
            }
        } else if (repairObject != null) {
            repairObject.click(true);
            while (repairObject != null && longTimer.isRunning()) {
                Task.sleep(300, 500);
            }
        } else if (reinforceObject != null) {
            System.out.println("Found temple wall");
            if (Players.getLocal().getAnimation() != reinforceAnimation) {
                reinforceObject.click(true);
                while (reinforceObject != null && longTimer.isRunning()) {
                    Task.sleep(300, 500);
                }
            }
        } else {
            System.out.println("Mysterious");
        }
    }

    public static void updateTempleStatus() {
        if (templeScoresWidget.validate() && templeScoresWidget.getChild(1).getText() != null) {
            templeStatus = Integer.parseInt(templeScoresWidget.getChild(1).getText());
        }
    }

    public static void updateResourceCount() {
        if (templeScoresWidget.validate() && templeScoresWidget.getChild(3).getText() != null) {
            resourcesCount = Integer.parseInt(templeScoresWidget.getChild(3).getText());
        }
    }

    //REMAKE
    private static void attackShade() {
        NPC shade = NPCs.getNearest(shadeId);
        if (shade != null && shade.isOnScreen() && !Players.getLocal().isInCombat()) {
            System.out.println("Attacks a shade");
            shade.click(true);
        }
    }
}
