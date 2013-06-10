package sdnscripts.shadesofmortton;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

/**
 *
 * @author Alaineman
 */
public class Travel {
    
    public static Timer runTimer = new Timer(4500);
    
    public static void travel(Tile direction) {
        Tile gateNorth = new Tile(3485, 3244, 0);
        if (Players.getLocal().getLocation().getY() >= 3244) {
            if (direction.getY() >= 3244) {
                travelSub(direction);
            } else {
                travelSub(gateNorth);
                jumpGate();
            }
        } else {
            if (direction.getY() < 3244) {
                travelSub(direction);
            } else {
                travelSub(gateNorth.derive(0, -1));
                jumpGate();
            }
        }
    }

    public static void jumpGate() {
        SceneObject gate = SceneEntities.getNearest(17757);
        if (gate != null && gate.isOnScreen()) {
            gate.click(true);
            runTimer.reset();
            while (runTimer.isRunning() && Players.getLocal().getAnimation() != 2923) {
                Task.sleep(300, 500);
            }
            runTimer.reset();
            while (runTimer.isRunning() && Players.getLocal().getAnimation() == 2923) {
                Task.sleep(300, 500);
            }
        }
    }

    public static void travelSub(Tile direction) {
        Timer run = new Timer(50000);
        while (run.isRunning() && Calculations.distanceTo(direction) > 2) {
            Walking.walk(direction);
            runTimer.reset();
            while (runTimer.isRunning() && Walking.getDestination() != null && Calculations.distanceTo(Walking.getDestination()) > Random.nextInt(3, 5)) {
                Task.sleep(300, 500);
            }
        }
    }
    
}
