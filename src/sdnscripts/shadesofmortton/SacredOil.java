package sdnscripts.shadesofmortton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.util.Random;

/*
 * Improve combat system
 * Add status report
 * 
 * @author Alaineman
 */
@Manifest(authors = {"Alaineman"}, name = "Sacred Oil Maker", description = "Big wet oily butts!", version = 0.45, topic = 1000454, vip = false, hidden = false)
public class SacredOil extends ActiveScript {

    /**
     * A List of Node elements which will push to the loop each time.
     */
    public static final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());    
    /**
     * A tree structure in which the Node's are processed in the loop.
     */
    public static Tree jobContainer = null;
    /**
     * If canceled is set to true the script will stop.
     */
    public static boolean canceled = false;    
    /**
     * All the Sacred Oil id's.
     * They are listed from 1 to 4 dose.
     * For example: sacredOils[2] is a 3 dose vial
     * and sacredOils[0] is a 1 dose vial.
     */
    public static int[] sacredOils = {3436, 3434, 3432, 3430};
    
    /**
     * Same as sacredOils but then with oliveOils.
     */
    public static int[] oliveOils = {3428, 3426, 3424, 3422};    
    /**
     * The boolean option to build or fight.
     */
    public static boolean fightModus = false;
    /**
     * The ShopList needed to keep track of the store.
     */
    public static ShopList sl;

    public static synchronized void provide(final Node... jobs) {
        for (final Node job : jobs) {
            if (!jobsCollection.contains(job)) {
                jobsCollection.add(job);
            }
        }
        jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }

    @Override
    public void onStart() {
        JFrame gui = new SOMGUI("Sacred GUI");
        gui.setVisible(true); 
        sl = new ShopList();
        sl.setShopBeams(1000);
        sl.setShopBricks(1000);
        sl.setShopOils(500);
    }

    @Override
    public int loop() {
        if(canceled){
            JOptionPane.showMessageDialog(null, "Thanks for using Sacred Oil Maker");
            stop();
        }
        if (jobContainer != null) {
            final Node job = jobContainer.state();
            if (job != null) {
                jobContainer.set(job);
                getContainer().submit(job);
                job.join();
            }
        }
        return Random.nextInt(400, 700);
    }    
}
