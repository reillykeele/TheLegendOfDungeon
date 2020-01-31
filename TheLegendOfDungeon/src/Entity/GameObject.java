/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import World.Map;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author 28783
 */
public class GameObject {

    //GAMEOBJECT IS THE TOP CLASS.
    //ALL OBJECTS WILL EXTEND GAMEOBJECT, OR A CHILD OF GAMEOBJECT. 
    //VARS
    protected Map map;

    //MOVEMENT VARS
    protected double x, y;
    protected double dx, dy;

    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;

    //STATE / TILEMAP / ROOM ? 
    //SIZE
    protected int width, height;
    protected int cwidth, cheight;

    //ANIMATION
    protected Animation[] animations;
    protected int currAnimation;

    //CONSTRUCTOR
    public GameObject(Map map, double x, double y) {
        this.map = map;
        this.x = x;
        this.y = y;
    }

    //GETTERS AND SETTERS
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCwidth() {
        return cwidth;
    }

    public int getCheight() {
        return cheight;
    }

    public Rectangle getRect(int x, int y) {
        return new Rectangle((int) x, (int) y, cwidth, cheight);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        xtemp = x;
        ytemp = y;
    }

    public void setAnimation(int anim) {
//        if (currAnimation != anim) {
//            animations[currAnimation].stop();
//            currAnimation = anim;
//            animations[anim].start();
//        } else {
//            animations[anim].start();
//        }
    }
    
    public void animate() {
        if (animations[currAnimation] != null) {
            animations[currAnimation].update();
        }
    }

    public void update() {
        animate();
    }

    public void render(Graphics2D g) {
//        g.drawImage(animations[currAnimation].getSprite(), (int) (x * GamePanel.SCALE - state.getCamera().getXOffset()) - (width - (int) (GamePanel.TILESIZE * GamePanel.SCALE)), (int) (y * GamePanel.SCALE - state.getCamera().getYOffset()) - (height - (int) (GamePanel.TILESIZE * GamePanel.SCALE)), width, height, null);

//        g.drawRect((int) (Map.xdis + x), (int) (Map.ydis + y ),
//                32, 32);

        if (animations[currAnimation] != null && !map.transition) {
            
            if(animations[currAnimation].getWidth() > width || animations[currAnimation].getHeight() > height) {
            g.drawImage(animations[currAnimation].getSprite(), (int) (Map.xdis + x + (cwidth - animations[currAnimation].getWidth()) / 2), (int) (Map.ydis + y + (cheight - animations[currAnimation].getHeight()) / 2),
                    animations[currAnimation].getWidth(), animations[currAnimation].getHeight(), null);
            } else {
                g.drawImage(animations[currAnimation].getSprite(), (int) (Map.xdis + x + (cwidth - width) / 2), (int) (Map.ydis + y + (cheight - height)),
                    animations[currAnimation].getWidth(), animations[currAnimation].getHeight(), null);
            }
        }
    }

    public void render(Graphics2D g, int xx, int yy) {
//        g.drawImage(animations[currAnimation].getSprite(), (int) (x * GamePanel.SCALE - state.getCamera().getXOffset()) - (width - (int) (GamePanel.TILESIZE * GamePanel.SCALE)), (int) (y * GamePanel.SCALE - state.getCamera().getYOffset()) - (height - (int) (GamePanel.TILESIZE * GamePanel.SCALE)), width, height, null);

//        g.drawRect((int) (Map.xdis + x), (int) (Map.ydis + y ),
//                32, 32);
        if (animations[currAnimation] != null) {
            g.drawImage(animations[currAnimation].getSprite(), (int) (xx + Map.xdis + x + (cwidth - width) / 2), (int) (yy + Map.ydis + y - (cheight - height)),
                    animations[currAnimation].getWidth(), animations[currAnimation].getHeight(), null);
        }
    }
}
