/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Enemies;

import Entity.Entity;
import Main.GamePanel;
import World.Map;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author 28783
 */
public class Enemy extends Entity {

    //BAT ENEMY: 
    //  PICK A RANDOM DEGREE FROM [0, 360] FOR DIRECTION
    //  INCREMENT / DECREMENT RANDOMLY BY SET AMOUNT. 
    //  IF COLLIDES WITH WALL OR OBJECT, FLIP DIRECTIONS. 
    //
    //  EX. 1 = INC, 0 = DEC, RND DIR = 90, SET AMOUNT = 10
    //  1 -> 90 + 10 = 100
    //  1 -> 100 + 10 = 110
    //  0 -> 110 - 10 = 100 . . . 
    public Enemy(Map map, double x, double y) {
        super(map, x, y);

        cwidth = GamePanel.TILESIZE;
        cheight = GamePanel.TILESIZE;

        health = 3;
    }

    @Override
    public void calcDirection() {

    }

    @Override
    public void attack(int direction) {

    }

    @Override
    public void update() {

        if (!map.transition && !flinching) {
            //getNextPosition(6 * Math.round(dt));

            checkTileMapCollision();
            setPosition(xtemp, ytemp);
        }

    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if (!map.transition) {

            //draw ENEMY
            g.setColor(Color.GREEN);
            //g.fillRect((int) (Map.xdis + x), (int) (Map.ydis + y), 32, 32);

        }
    }

}
