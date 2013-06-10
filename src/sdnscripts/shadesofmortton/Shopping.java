package sdnscripts.shadesofmortton;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 *
 * @author Alaineman
 */
public class Shopping {

    public static Tile shop = new Tile(3488, 3294, 0);
    public static NPC shopKeeper = NPCs.getNearest(7899);
    public static SceneObject closedDoor = SceneEntities.getNearest(1530);
    public static WidgetChild storeWidget = Widgets.get(1265, 26);
    public static WidgetChild currentItem = Widgets.get(1265, 39);
    public static WidgetChild plusFive = Widgets.get(1265, 212);
    public static WidgetChild plusOne = Widgets.get(1265, 71);
    public static WidgetChild buyCount = Widgets.get(1265, 68);
    public static WidgetChild buyButton = Widgets.get(1265, 207);

    public static void shopOil() {
        if (Calculations.distanceTo(shop) > 4) {
            Travel.travel(shop);
        } else {
            if (closedDoor != null && closedDoor.getLocation().equals(shop)) {
                closedDoor.click(true);
                return;
            }

            if (shopKeeper == null || Calculations.distanceTo(shopKeeper) > 5) {
                Travel.travel(shop);
            }
            if (storeWidget.validate()) {
                WidgetChild oil = storeWidget.getChild(7);
                if (oil.validate()) {
                    oil.interact("Buy 50");
                    Altar.runTimer.reset();
                    while (Altar.runTimer.isRunning() && !Inventory.isFull()) {
                        Task.sleep(400, 600);
                    }
                    SacredOil.sl.setShopOils(oil.getChildStackSize());
                }
            } else if (shopKeeper.interact("Trade-General-Store")) {
                Altar.runTimer.reset();
                while (Altar.runTimer.isRunning() && !storeWidget.validate()) {
                    Task.sleep(400, 600);
                }
            }
        }
    }

    public static void shopMaterials(int child) {
        if (Calculations.distanceTo(shop) > 4) {
            Travel.travel(shop);
        } else {
            if (checkDoor()) {
                return;
            }
            if (shopKeeper == null || Calculations.distanceTo(shopKeeper) > 5) {
                Travel.travel(shop);
            }
            if (storeWidget.validate()) {
                WidgetChild material = storeWidget.getChild(child);
                if (storeWidget.validate()) {
                    material.click(true);
                    Altar.runTimer.reset();
                    while (Altar.runTimer.isRunning() && !(currentItem.getText().equals("Limestone brick") || currentItem.getText().equals("Timber beam"))) {
                        Task.sleep(400, 600);
                    }
                    plusFive("6");
                    plusFive("11");
                    plusOne("12");
                    plusOne("13");
                    buyProduct();
                    if (child == 1) {
                        SacredOil.sl.setShopBricks(material.getChildStackSize());
                    } else if (child == 2) {
                        SacredOil.sl.setShopBeams(material.getChildStackSize());
                    }
                }
            } else if (shopKeeper.interact("Trade-Builders-Store")) {
                Altar.runTimer.reset();
                while (Altar.runTimer.isRunning() && !storeWidget.validate()) {
                    Task.sleep(400, 600);
                }
            }
            checkDoor();
        }
    }

    public static boolean checkDoor() {
        if (closedDoor != null && closedDoor.getLocation().equals(shop)) {
            return closedDoor.click(true);
        }
        return false;
    }

    public static void buyProduct() {
        int oldInventory = Inventory.getCount();
        buyButton.click(true);
        Altar.runTimer.reset();
        while (Altar.runTimer.isRunning() && Inventory.getCount() != oldInventory) {
            Task.sleep(400, 600);
        }
    }

    public static void plusOne(String number) {
        plusOne.click(true);
        Altar.runTimer.reset();
        while (Altar.runTimer.isRunning() && !buyCount.getText().equals(number)) {
            Task.sleep(400, 600);
        }
    }

    public static void plusFive(String number) {
        plusFive.click(true);
        Altar.runTimer.reset();
        while (Altar.runTimer.isRunning() && !buyCount.getText().equals(number)) {
            Task.sleep(400, 600);
        }
    }
}
