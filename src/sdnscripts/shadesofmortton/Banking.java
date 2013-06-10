package sdnscripts.shadesofmortton;

import javax.swing.JOptionPane;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 *
 * @author Alaineman
 */
public class Banking extends Node{
    
    public static Tile bankTile = new Tile(3496, 3211, 0);  
    public static NPC shopKeeper = NPCs.getNearest(7899);
    public static SceneObject closedDoor = SceneEntities.getNearest(1530);
    public static WidgetChild storeWidget = Widgets.get(1265, 26);
    public static WidgetChild currentItem = Widgets.get(1265, 39);
    public static WidgetChild plusFive = Widgets.get(1265, 212);
    public static WidgetChild plusOne = Widgets.get(1265, 71);
    public static WidgetChild buyCount = Widgets.get(1265, 68);
    public static WidgetChild buyButton = Widgets.get(1265, 207);

    @Override
    public boolean activate() {
        return Inventory.getCount(SacredOil.oliveOils) == 0 && Inventory.getCount(SacredOil.sacredOils) > 0;
    }

    @Override
    public void execute() {
        if (Calculations.distanceTo(bankTile) <= 4) {
            if (Bank.open()) {
                for (int i = 3; i >= 0; i--) {
                    if (Inventory.getCount(SacredOil.sacredOils[i]) > 0) {
                        Bank.deposit(SacredOil.sacredOils[i], Bank.Amount.ALL);
                        Travel.runTimer.reset();
                        while (Inventory.getCount(SacredOil.sacredOils[i]) != 0 && Travel.runTimer.isRunning()) {
                            Task.sleep(300, 500);
                        }
                    }
                }
            }
        } else {
            Travel.travel(bankTile);
        }
    }
    
    public static boolean getOilsFromBank() {
        if (Calculations.distanceTo(bankTile) <= 4) {
            if (Bank.open()) {
                for (int i = 3; i >= 0; i--) {
                    Item oliveOil = Bank.getItem(SacredOil.oliveOils[i]);
                    if (oliveOil != null && oliveOil.getStackSize() > 28) {
                        Bank.withdraw(SacredOil.oliveOils[i], Bank.Amount.ALL);
                        Altar.runTimer.reset();
                        while (Altar.runTimer.isRunning() && !Inventory.isFull()) {
                            Task.sleep(300, 500);
                        }
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "You run out of oils, both shop and bank.");
                        SacredOil.canceled = true;
                    }
                }
            }
        } else {
            Travel.travel(bankTile);
        }
        return false;
    }
    
    public static boolean withdrawFromBank(int id, int minimum) {
        if (Calculations.distanceTo(bankTile) <= 4) {
            if (Bank.open()) {
                Item item = Bank.getItem(id);
                if (item != null && item.getStackSize() > minimum) {
                    Bank.withdraw(id, 13);
                    Altar.runTimer.reset();
                    while (Inventory.getCount(id) != 0 && Altar.runTimer.isRunning()) {
                        Task.sleep(300, 500);
                    }
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "You run out of materials, both shop and bank.");
                    SacredOil.canceled = true;
                }
            }
        } else {
            Travel.travel(bankTile);
        }
        return false;
    }

    public static boolean withdrawFromBank(int id, int minimum, Bank.Amount bAmount) {
        if (Calculations.distanceTo(bankTile) <= 4) {
            if (Bank.open()) {
                Item oliveOil = Bank.getItem(id);
                if (oliveOil != null && oliveOil.getStackSize() > minimum) {
                    Bank.withdraw(id, bAmount);
                    Altar.runTimer.reset();
                    while (Inventory.getCount(id) != 0 && Altar.runTimer.isRunning()) {
                        Task.sleep(300, 500);
                    }
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "You run out of oils, both shop and bank.");
                    SacredOil.canceled = true;
                }
            }
        } else {
            Travel.travel(bankTile);
        }
        return false;
    }
    
}
