package sdnscripts.pestcontrol;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.SceneObject;

public  class GamePlay extends Node {

        NPC voidKnight = NPCs.getNearest(3784, 3785);
        NPC monster = NPCs.getNearest(PestMain.game.getMonsters());

        @Override
        public boolean activate() {
            return PestMain.game.isInGame();
        }

        @Override
        public void execute() {
            if (PestMain.game.getActivePortalId() == -1l) {
                if (monster != null) {
                    attackMonster();
                } else {
                    Tile monsterTile = voidKnight.getLocation().derive(0, -12);
                    if (Calculations.distanceTo(monsterTile) > 8) {
                        Walking.walk(monsterTile);
                        Task.sleep(300, 600);
                    }
                }
            } else if (PestMain.game.getPortalModus()) {
                SceneObject portalObject = SceneEntities.getNearest(PestMain.game.getActivePortalId());
                if (!Players.getLocal().isInCombat()) {
                    if (!portalObject.isOnScreen()) {
                        Walking.walk(portalObject);
                        Camera.turnTo(portalObject);
                        Task.sleep(300, 600);
                        return;
                    }
                    portalObject.click(true);
                }
            } else if (PestMain.game.getVoidModus() && voidKnight != null) {
                if (Calculations.distanceTo(voidKnight) < 5) {
                    if (monster != null) {
                        attackMonster();
                    }
                } else {
                    Walking.walk(voidKnight);
                    PestMain.t.reset();
                    while (PestMain.t.isRunning() && !voidKnight.isOnScreen()) {
                        Task.sleep(200, 400);
                    }
                }
            }
        }

        private void attackMonster() {
            if (!monster.isOnScreen()) {
                Camera.turnTo(monster);
                PestMain.t.reset();
                while (PestMain.t.isRunning() && !monster.isOnScreen()) {
                    Task.sleep(200, 400);
                }
            }
            monster.interact("Attack");
        }
    }