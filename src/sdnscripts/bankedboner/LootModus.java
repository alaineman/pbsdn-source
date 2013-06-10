package sdnscripts.bankedboner;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.Item;

/*
 * 
 * @author Alaineman
 */
public class LootModus extends Node {

    private Timer t = new Timer(600);

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void execute() {
        BBB5.statusUpdate(false);
        GroundItem g = GroundItems.getNearest(BBB5GUI.BONES[BBB5.statusSelection]);
        if (!Inventory.isFull() && g != null) {
            if (g.isOnScreen()) {
                g.interact("Take");
            } else {
                Camera.turnTo(g);
            }
        } else {
            Item item = Inventory.getItem(BBB5GUI.BONES);
            if (item != null) {
                item.getWidgetChild().click(true);
                t.reset();
                while (t.isRunning() && item != null) {
                    Task.sleep(50, 75);
                }
            }
        }
    }

    
}
