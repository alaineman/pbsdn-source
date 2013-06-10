package sdnscripts.pestcontrol;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.Widget;

public class Board extends Node {

    //Widget in the boat
    final Widget INFO_WIDGET = Widgets.get(407);

    @Override
    public boolean activate() {
        return !PestMain.game.isInGame() && !INFO_WIDGET.validate() && !PestMain.spendTickets && PestMain.difficultySelection != -1;
    }

    @Override
    public void execute() {
        final int[] PLANK_IDS = {14315, 25631, 25632};
        SceneObject plank = SceneEntities.getNearest(PLANK_IDS[PestMain.difficultySelection]);
        if (plank == null) {
            switch (PestMain.difficultySelection) {
                case 0:
                    Walking.walk(new Tile(2657, 2639, 0));
                    break;
                case 1:
                    Walking.walk(new Tile(2644, 2644, 0));
                    break;
                case 2:
                    Walking.walk(new Tile(2638, 2653, 0));
                    break;
            }
        }
        if (!plank.isOnScreen()) {
            Walking.walk(plank);
            return;
        }
        if (plank.interact("Cross")) {
            Timer t = new Timer(5000);
            while (t.isRunning() && !INFO_WIDGET.validate()) {
                sleep(200, 400);
            }
        }
        if (INFO_WIDGET.validate()) {
            String pointText = INFO_WIDGET.getChild(16).getText();
            if (!pointText.isEmpty()) {
                String[] textElements = pointText.split(":");

                if (textElements != null && textElements.length > 1) {
                    int pointAmount = Integer.parseInt(textElements[1].trim());
                    if (pointAmount > 450) {
                        PestMain.spendTickets = true;
                    }
                }
            }
        }

        PestMain.t.reset();
        while (PestMain.t.isRunning() && Calculations.distanceTo(Walking.getDestination()) > 4) {
            Task.sleep(300, 500);
        }
    }
}