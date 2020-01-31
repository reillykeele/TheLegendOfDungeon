/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Enemies;

import Entity.Animation;
import Main.GamePanel;
import World.Map;
import java.awt.Graphics2D;

/**
 *
 * @author 28783
 */
public class Bat extends Enemy {

    //DIRECTION IS AN INTEGER FROM [0, 360]
    private int direction;

    private static final int INCREMENTER = 3;

    public Bat(Map map, double x, double y) {
        super(map, x, y);
        
        getRandomDirection();

        width = 32;
        height = 32;
        cwidth = GamePanel.TILESIZE;
        cheight = GamePanel.TILESIZE;

        health = 2;
        speed = 2;
        damage = 1;

        try {

            animations = new Animation[1];
            //public Animation(String file, long frameLength, int numFrames, int width, int height, boolean animated, boolean loop) {
            animations[0] = new Animation("/Resources/Images/Sprites/Bat/batSheet.gif", (long) 50000000, 5, 64, 30, true, true);

            currAnimation = 0;

            animations[currAnimation].start();

        } catch (Exception e) {

        }
    }
    
    public void getRandomDirection() {
        direction = (int) -(Math.random() * 360);
    }

    @Override
    public void calcDirection() {
        //1 = INC, 0 = DEC
        int rnd = (int) Math.round(Math.random());

        if (rnd == 1) {
            direction += INCREMENTER;
        } else {
            direction -= INCREMENTER;
        }

    }

    @Override
    public void collision(boolean tl, boolean tr, boolean bl, boolean br) {
//        System.out.println("------------------------------------");
//        System.out.println("TL: " + topLeft);
//        System.out.println("TR: " + topRight);
//        System.out.println("BL: " + bottomLeft);
//        System.out.println("BR: " + bottomRight);

        if (topLeft || topRight || bottomLeft || bottomRight) {

//            //if going right
//            if (direction >= -90 && direction <= 0) {
//                direction = -360 - direction;
//            } else if(direction < -90 && direction <= -180) {
//                direction = -360 - direction;
//            } else if (direction < -180 && direction >= -270) {
//                direction = -360 - direction;
//            } else if(direction < -360) {
//                direction = -360 - direction;
//            }

            getRandomDirection();

            //direction = -90 + direction;
            
            //System.out.println(direction);

        }
    }

//    @Override
//    public void checkTileMapCollision() {
//        super.checkTileMapCollision();
//        
//        
//
//    }
    @Override
    public void getNextPosition(double speed) {

        double pheta = Math.toRadians(direction);

        //X = COS
        //COS = ADJ / HYP
        //HYP * COS = ADJ
        dx = speed * Math.cos(pheta);

        //Y = SIN
        dy = speed * Math.sin(pheta);

    }

    @Override
    public void update() {
        super.update();
        animate();

        //System.out.println(direction);
        if (direction > 0) {
            direction = (-360 - (direction));
        } else if (direction <= -360) {
            direction = (360 + direction);
        } 

        if(!flinching) {
        getNextPosition(speed);
        calcDirection();
        } else {
            flinch();
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
    }

}
