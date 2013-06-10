package sdnscripts.scaffoldbuilder;

import org.powerbot.game.api.wrappers.Tile;

/**
 *
 * @author Vincent W
 */
public class BuildingSite {
    //Differend states
    //-1 not to be done
    // 0 is not done yet but in queue
    // 1 is currently in process (travelling or building) in queue
    // 2 is done (not in queue / dequeued)

    private Tile location;
    private int builder;
    //True is prayer, false is slayer
    private boolean reward;
    private String nm;

    public BuildingSite(Tile location, int builder, boolean xp, String name) {
        this.location = location;        
        this.builder = builder;
        this.reward = xp;
        this.nm = name;
    }

    public String getName() {
        return nm;
    }

    public void setName(String nm) {
        this.nm = nm;
    }

    public boolean getReward() {
        return reward;
    }

    public void setReward(boolean reward) {
        this.reward = reward;
    }

    public Tile getLocation() {
        return location;
    }

    public void setLocation(Tile location) {
        this.location = location;
    }

    public int getBuilder() {
        return builder;
    }

    public void setBuilder(int id) {        
        this.builder = id;
    }
}