/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import World.Map;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author ReillyKeele
 */
public class Chest extends GameObject{
    
    public static final int COINS = 0;
    public static final int BOSSKEY = 1;
    
    public static final int CLOSED = 0;
    public static final int OPEN = 1;
    
    private int item;
    
    private boolean opened = false;
    
    public Chest(Map map, double x, double y, int item) {
        super(map, x, y);
        
        this.item = item;
        
        cwidth = 32;
        cheight = 32;
        
        width = 32;
        height = 32;
        
        animations = new Animation[2];
        
        //0 closed 1 open
        //animations[NIDLE] = new Animation("src/Resources/Images/Sprites/Player/playerIdleNorth.gif", (long) 50000000, 1, 32, 48, false, false);
        
        //BufferedReader inputStream = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/Resources/Images/Items/chestClosed.gif")));
        
        animations[0] = new Animation("/Resources/Images/Items/chestClosed.gif", (long) 50000000, 1, 32, 32, false, false);
        animations[1] = new Animation("/Resources/Images/Items/chestOpen.gif", (long) 50000000, 1, 32, 32, false, false);
        
        currAnimation = 0;        
        
        animations[0].start();
    }
    
    public void setOpen(boolean b) {
        opened = b;
        
        if(opened) {
            currAnimation = 1;
            animations[currAnimation].start();
        }
    }
    
    public boolean getOpen() {
        return opened;
    }
    
    public int getItem() {
        return item;
    }
    
//    @Override
//    public void render(Graphics2D g) {
//        super.render(g);        
//    }
    
}
