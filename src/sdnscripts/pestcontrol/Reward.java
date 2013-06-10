package sdnscripts.pestcontrol;

public enum Reward {
    ATTACK(27,88,100), STRENGTH(27,104,100), DEFENCE(27,120,100), CONSTITUTION(27,136,100),
    RANGED(27,152,100), MAGIC(27,168,100), PRAYER(27,184,100), MELEE_HELM(29,15,200), TOP(29,196,250),
    ROBES(29,208,250), RANGE_HELM(29,220,200), MAGE_HELM(29,232,200), GLOVES(29,244,150), 
    MACE(29,256,250), DEFLECTOR(29,268,150), SEAL(29,280,10), HERB(31,291,30), MINERAL(31,302,15),
    SEED(31,313,15), SPINNER(31,328,450), TORCHER(31,343,450), RAVAGER(31,358,450), SHIFTER(31,373,450);
    
    private final int tabId;
    private final int childId;
    private final int minimal;
    
    private Reward(int tab, int child, int lowest) {
        tabId = tab;
        childId = child;
        minimal = lowest;
    }
    
    public int getTab(){
        return tabId;                
    }
    
    public int getChildId(){
        return childId;
    }
    
    public int getMinimalAmount(){
        return minimal;
    }
    
}
