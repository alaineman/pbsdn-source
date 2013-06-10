package sdnscripts.shadesofmortton;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.tab.Inventory;

/**
 *
 * @author Alaineman
 */
public class FightModus extends Node {

    @Override
    public boolean activate() {
        return !SacredOil.canceled && !(Inventory.getCount(SacredOil.oliveOils)==0 && Inventory.getCount(SacredOil.sacredOils)>0);
    }

    @Override
    public void execute() {
        if (Inventory.getCount(SacredOil.oliveOils) > 0) {
             if (Calculations.distanceTo(Altar.altar) > 6) {
                    Travel.travel(Altar.altar);
                } 
             Altar.makeSacredOils();
        } else if (Inventory.getCount(SacredOil.oliveOils) == 0 && Inventory.getCount(SacredOil.sacredOils) == 0) {
            if (!SacredOil.sl.shopStop(SacredOil.fightModus)) {
                Banking.getOilsFromBank();
            } else {
                Shopping.shopOil();
            }
        }
    }
}
