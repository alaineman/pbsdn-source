package sdnscripts.bankedboner;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/*
 * 
 * @author Alaineman
 */
public class AbilityBarBury extends Node {

    private Widget bar = Widgets.get(640);
    private Timer t = new Timer(3000);
    private WidgetChild target = bar.getChild(66);
    private WidgetChild charChild = bar.getChild(115);
    private boolean itemIsSet = false;
    private int BONE_ID = BBB5GUI.BONES[BBB5.statusSelection];

    @Override
    public boolean activate() {
        return Inventory.getCount(BONE_ID) > 0;
    }

    @Override
    public void execute() {
        BBB5.statusUpdate(false);
        setBarExpended();
        if (!itemIsSet) {
            if (!bar.validate()) {
                return;
            }
            Item itemToSet = Inventory.getItem(BONE_ID);
            if (itemToSet == null) {
                return;
            }
            if (bar.getChild(112).getChildId() == BONE_ID) {
                itemIsSet = true;
                return;
            }
            Inventory.selectItem(itemToSet);
            t.reset();
            while (t.isRunning() && !Inventory.isItemSelected()) {
                Task.sleep(50, 100);
            }
            WidgetChild lock = bar.getChild(26);
            if (lock != null && lock.visible() && lock.getTextureId() == 14286) {
                lock.click(true);
                t.reset();
                while (t.isRunning() && lock.getTextureId() != 14289) {
                    Task.sleep(50, 100);
                }
            }
            if (dragToBar(target)) {
                itemIsSet = true;
            }
        } else {

            if (target.visible()) {
                Timer timeOut = new Timer(50000);
                while (Inventory.getCount(BONE_ID) > 0 && timeOut.isRunning()) {
                    char key = charChild.getText().trim().charAt(0);
                    Keyboard.pressKey(key, 0, 0);
                    Task.sleep(50, 100);
                    Keyboard.releaseKey(key, 0, 0);
                    //System.out.println("Character to send: " + key);
                    Task.sleep(100, 200);
                }
            }
        }
    }

    private boolean dragToBar(WidgetChild start) {
        if (start == null || !start.visible() || !bar.validate()) {
            return false;
        }
        if (target == null) {
            return false;
        }
        Mouse.drag(target.getNextViewportPoint());
        return true;
    }

    private boolean setBarExpended() {
        WidgetChild miniBar = bar.getChild(2);
        WidgetChild maxBarButton = bar.getChild(3);
        WidgetChild maxiBar = bar.getChild(21);
        if (miniBar.validate() && miniBar.visible()) {
            if (maxBarButton.validate() && maxBarButton.visible() && maxBarButton.click(true)) {
                t.reset();
                while (t.isRunning() && !(maxiBar.validate() && maxiBar.visible())) {
                    Task.sleep(50, 100);
                }
                return true;
            }
            return false;
        } else {
            return true;
        }
    }
}
