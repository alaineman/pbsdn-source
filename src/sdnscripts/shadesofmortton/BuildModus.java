package sdnscripts.shadesofmortton;

import javax.swing.JOptionPane;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

/**
 *
 * @author Alaineman
 */
public class BuildModus extends Node {

    private int brick = 3420;
    private int beam = 8837;
    private int beamChild = 2;
    private int brickChild = 1;
    private int swampTar = 1941;

    @Override
    public boolean activate() {
        return !SacredOil.canceled && !(Inventory.getCount(SacredOil.oliveOils)==0 && Inventory.getCount(SacredOil.sacredOils)>0);
    }

    @Override
    public void execute() {
        if (Inventory.getItem(swampTar) == null || Inventory.getItem(swampTar).getStackSize() < 80) {
            JOptionPane.showMessageDialog(null, "You run out of swamp tar in your inventory.");
            SacredOil.canceled = true;
        }
        Altar.updateResourceCount();
        if (Altar.resourcesCount <= 17 || Inventory.getCount(beam) > 0) {
            if (Inventory.getCount(beam) == 0 || Inventory.getCount(brick) == 0) {
                if (!SacredOil.sl.shopBeams()) {
                    if (Banking.withdrawFromBank(beam, 13)) {
                        return;
                    }
                }
                if (!SacredOil.sl.shopBricks()) {
                    if (Banking.withdrawFromBank(brick, 13)) {
                        return;
                    }
                }
                if (!Inventory.contains(beam)) {
                    Shopping.shopMaterials(beamChild);
                }
                if (!Inventory.contains(brick)) {
                    Shopping.shopMaterials(brickChild);
                }
            } else {
                if (Calculations.distanceTo(Altar.altar) > 6) {
                    Travel.travel(Altar.altar);
                } else {
                    Altar.repairTemple();
                }
            }
        } else {
            if (Inventory.getCount(SacredOil.oliveOils) == 0) {
                if (!SacredOil.sl.shopOils()) {
                    if (Banking.withdrawFromBank(SacredOil.oliveOils[2], 26, Bank.Amount.ALL)) {
                    }
                } else {
                    Shopping.shopOil();
                }
            } else {
                if (Calculations.distanceTo(Altar.altar) > 3) {
                    Travel.travel(Altar.altar);
                } else {
                    Altar.makeSacredOils();
                }
            }
        }
    }
}
