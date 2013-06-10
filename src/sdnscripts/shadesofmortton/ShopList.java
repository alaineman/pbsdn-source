/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sdnscripts.shadesofmortton;

/**
 *
 * @author Alaineman
 */
public class ShopList {

    private int shopBeams;
    private int shopBricks;
    private int shopOils;   

    public ShopList() {
        this.shopBeams = 0;
        this.shopBricks = 0;
        this.shopOils = 0;       
    }

    public int getShopBeams() {
        return shopBeams;
    }

    public void setShopBeams(int shopBeams) {
        this.shopBeams = shopBeams;
    }

    public int getShopBricks() {
        return shopBricks;
    }

    public void setShopBricks(int shopBricks) {
        this.shopBricks = shopBricks;
    }

    public int getShopOils() {
        return shopOils;
    }

    public void setShopOils(int shopOils) {
        this.shopOils = shopOils;
    }  

    public boolean shopBricks() {
        return shopBricks > 10;
    }

    public boolean shopBeams() {
        return shopBeams > 10;
    }

    public boolean shopOils() {
        return shopOils > 28;
    }
    
    public boolean shopStop(boolean fightModus){
        if(fightModus){
            return shopOils();
        }
        return shopBeams() || shopBricks() || shopOils();
    }    
}
