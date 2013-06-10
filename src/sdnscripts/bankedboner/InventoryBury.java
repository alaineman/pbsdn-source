package sdnscripts.bankedboner;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;

/*
 * 
 * @author Alaineman
 */
public class InventoryBury extends Node {

    private Timer t = new Timer(500);

    @Override
    public boolean activate() {
        return Inventory.getCount(BBB5GUI.BONES) > 0;
    }

    @Override
    public void execute() {
        BBB5.statusUpdate(false);
        Item[] items = Inventory.getItems();
        if(items == null){
            return;
        }
        for (Item i : items) {
            if (i != null && arrayIndexOf(BBB5GUI.BONES, i.getId())) {
                i.getWidgetChild().click(true);
                t.reset();
                while (t.isRunning() && i != null) {
                    Task.sleep(50, 75);
                }
            }
        }
    }

    private boolean arrayIndexOf(int[] arr, int id) {
        for (int k = 0; k < arr.length; k++) {
            if (arr[k] == id) {
                return true;
            }
        }
        return false;
    }
}
