package sdnscripts.pestcontrol;

import java.util.ArrayList;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.wrappers.widget.Widget;

public class Island {

    //Widget in the minigame (on the island).
    private Widget ingameWidget = Widgets.get(408);
    private boolean portalModus;
    private boolean voidModus;
    private ArrayList<Integer> monsters;
    private boolean[] portalStatus;

    public Island() {
        portalStatus = new boolean[8];
        monsters = new ArrayList<>();
        portalModus = false;
    }
    
    public void addMonster(int... i) {
        if (i != null && i.length > 0) {
            for (int id : i) {
                monsters.add(id);
            }
        }
    }

    public int[] getMonsters() {
        if (monsters.isEmpty()) {
            return null;
        }
        int size = monsters.size();
        int[] mIs = new int[size];
        for (int i = 0; i < size; i++) {
            mIs[i] = monsters.get(i);
        }
        return mIs;
    }

    public void setPortalModus(boolean b) {
        portalModus = b;
    }

    public boolean getPortalModus() {
        return portalModus;
    }

    public void setVoidModus(boolean b) {
        voidModus = b;
    }

    public boolean getVoidModus() {
        return voidModus;
    }

    public boolean isInGame() {
        return ingameWidget.validate();
    }

    public int getActivePortalId() {
        updatePortals();
        for (int i = 0; i < Portal.values().length; i++) {
            if (portalStatus[i]) {
                return Portal.values()[i].getUnshieldedId();
            }
        }
        return -1;
    }

    public void updatePortals() {
        if (isInGame()) {
            for (int i = 0; i < Portal.values().length; i++) {
                portalStatus[i] = ingameWidget.getChild(Portal.values()[i].getStatusChild()).visible();
            }
        }
    }
}
