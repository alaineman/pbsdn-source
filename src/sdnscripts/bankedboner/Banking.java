package sdnscripts.bankedboner;

import javax.swing.JOptionPane;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;

/*
 * 
 * @author Alaineman
 */
public class Banking extends Node {

    private int boneId;
    private Timer t = new Timer(5000);

    Banking(int i) {
        boneId = i;
    }

    @Override
    public boolean activate() {
        return Inventory.getCount(boneId) == 0;
    }

    @Override
    public void execute() {
        BBB5.statusUpdate(true);
        if (Bank.isOpen()) {
            if (Bank.getItemCount(boneId) > 0) {
                Bank.withdraw(boneId, Bank.Amount.ALL);
                t.reset();
                while (t.isRunning() && Inventory.getCount(boneId) == 0) {
                    Task.sleep(300, 500);
                }
                if (BBB5.paint != null) {
                    BBB5.paint.addBones(Inventory.getCount(boneId));
                }
                closeBank();
            } else {
                BBB5.canceled = true;
                JOptionPane.showMessageDialog(null, "You run out of bones.");
            }
        } else {
            Bank.open();
            t.reset();
            while (t.isRunning() && !Bank.isOpen()) {
                Task.sleep(500, 800);
            }
        }
    }

    private void closeBank() {
        Bank.close();
        t.reset();
        while (t.isRunning() && Bank.isOpen()) {
            Task.sleep(300, 500);
        }
        if (Bank.isOpen()) {
            closeBank();
        }

    }
}
