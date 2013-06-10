package sdnscripts.pestcontrol;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Exchange extends Node {

    Reward reward = null;
    int[] voidIds = {3789, 3788};
    NPC nearestVoid = NPCs.getNearest(voidIds);

    @Override
    public boolean activate() {
        return PestMain.spendTickets && nearestVoid != null && !PestMain.game.isInGame();
    }

    @Override
    public void execute() {
        Widget exchangeWidget = Widgets.get(1011);
        if (reward == null) {
            reward = PestMain.rewardQueue.poll();            
        }
        if (exchangeWidget.validate()) {
            WidgetChild rewardChild = exchangeWidget.getChild(reward.getChildId());
            if (!rewardChild.visible()) {
                exchangeWidget.getChild(reward.getTab()).click(true);
                PestMain.t.reset();
                while (PestMain.t.isRunning() && !rewardChild.visible()) {
                    Task.sleep(200, 400);
                }
            }
            rewardChild.click(true);
            Task.sleep(200, 400);
            int commendationPoints = Integer.parseInt(exchangeWidget.getChild(62).getText());
            if (commendationPoints < reward.getMinimalAmount()) {
                PestMain.spendTickets = false;
            }
            reward = null;
        }

        if (!nearestVoid.isOnScreen()) {
            Walking.walk(nearestVoid);
            return;
        }
        nearestVoid.interact("Exchange");
    }
}
