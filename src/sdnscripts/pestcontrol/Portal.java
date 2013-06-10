package sdnscripts.pestcontrol;

public enum Portal {   
    WEST(6146,6142,17), EAST(6147,6143,19), SOUTH_EAST(6148,6144,21), SOUTH_WEST(6149,6145,23),
    NOVICE_WEST(6154,6150,0), NOVICE_EAST(6155,6151,0), NOVICE_SOUTH_EAST(6156,6152,0), NOVICE_SOUTH_WEST(6157,6153,0);
    
    private final int SHIELD_ID;
    private final int OPEN_ID;
    private final int child;
    
    private Portal(int shieldID, int unshieldID, int statusChild) {
        SHIELD_ID = shieldID;
        OPEN_ID = unshieldID;
        child = statusChild;
    }
    
    public int getShieldedId(){
        return SHIELD_ID;
    }
    
    public int getUnshieldedId(){
        return OPEN_ID;
    }
    
    public int getStatusChild(){
        return child;
    }
        
}
