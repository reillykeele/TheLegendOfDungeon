/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Entity.Enemies.Enemy;
import Main.Audio;
import Main.GamePanel;
import World.Map;
import World.Room;
import World.Vector2;
import java.awt.Graphics2D;

/**
 *
 * @author 28783
 */
public class Entity extends GameObject {

    //PLAYER AND ALL AI(ENEMIES) WILL EXTEND GAMEOBJECT.
    //MAP / LEVEL 
    protected Vector2 currRoom;

    //COLLISION
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    //STATE / MOVEMENT
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean attack;
    protected boolean attacking;
    protected boolean shielding;
    protected boolean flinching;
    protected long lastFlinch;
    protected long flinchLength = (long) (1000000000);

    protected boolean blinking;
    protected long lastBlink;
    protected long timeBetweenBlinks = (long) (1000000000 * .1);

//    protected boolean jumping;
//    protected boolean falling;
    protected boolean pixelCorrectX;
    protected boolean pixelCorrectY;

    //DIRECTION
    protected int facing = 0;
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    //VECTORS
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;

    //HEALTH / DMG
    protected int health;
    protected int damage;

    protected int speed;

    //ATTACK 
    protected long lastAttack;
    protected long timeBetweenAttacks;
    protected long attackDuration;
    protected double knockback;

    public Entity(Map map, double x, double y) {
        super(map, x, y);

        xtemp = x;
        ytemp = y;

        xdest = x;
        ydest = y;

        currRoom = map.getCurrRoom();
    }
    
    public int getDamage() {
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int h) {
        health = h;
    }

    public int getDirection() {
        return facing;
    }

    public void setFlinching(boolean b) {

        if (b && flinching != b) {
            lastFlinch = System.nanoTime();
        }
        
        

        flinching = b;

    }

    public boolean getFlinching() {
        return flinching;
    }

    public void getNextPosition(double speed) {
//        if (left && !right) {
//            if (dx > -speed) {
//                dx -= speed;
//            } else {
//                dx = -speed;
//            }
//            //x += dx;
//        } else if (right && !left) {
//            if (dx < speed) {
//                dx += speed;
//            } else {
//                dx = speed;
//            }
//            //x += dx;
//        } else {
//            if (dx > 0) {
//                dx -= speed;
//                if (dx < 0) {
//                    dx = 0;
//                }
//            }
//            if (dx < 0) {
//                dx += speed;
//                if (dx > 0) {
//                    dx = 0;
//                }
//            }
//        }
//
//        if (up && !down) {
//            if (dy > -speed) {
//                dy -= speed;
//            } else {
//                dy = -speed;
//            }
//            y += dy;
//        } else if (down && !up) {
//            if (dy < speed) {
//                dy += speed;
//            } else {
//                dy = speed;
//            }
//            //y += dy;
//        } else {
//            if (dy < 0) {
//                dy += speed;
//                if (dy > 0) {
//                    dy = 0;
//                }
//            }
//            if (dy > 0) {
//                dy -= speed;
//                if (dy < 0) {
//                    dy = 0;
//                }
//            }
//
//        }
    }

    public void collision(boolean tl, boolean tr, boolean bl, boolean br) {

    }

    public void checkTileMapCollision() {
        
        if (dx==0 && dy == 0) {
            checkEntityCollision(x,y);
            return;
        }

        xdest = (x + dx);
        ydest = (y + dy);

        calculateCorners(xdest, ydest);

        //PIXEL CORRECTION FUNCTION
        if (pixelCorrectX) {
            if (up && topRight && !topLeft && dx == 0) {
                dx = -2;
            } else if (up && !topRight && topLeft && dx == 0) {
                dx = 2;
            }

            if (down && bottomRight && !bottomLeft && dx == 0) {
                dx = -2;
            } else if (down && !bottomRight && bottomLeft && dx == 0) {
                dx = 2;
            }
        }

        if (pixelCorrectY) {
            if (right && topRight && !bottomRight && dy == 0) {
                dy = 2;
            } else if (right && !topRight && bottomRight && dy == 0) {
                dy = -2;
            }

            if (left && topLeft && !bottomLeft && dy == 0) {
                dy = 2;
            } else if (left && !topLeft && bottomLeft && dy == 0) {
                dy = -2;
            }

        }

        collision(topLeft, topRight, bottomLeft, bottomRight);

        calculateCorners(x, ydest);

        if (dy < 0) {
            if (topLeft || topRight) {

                dy = 0;

                int yy = (int) (((int) ydest / GamePanel.TILESIZE) * GamePanel.TILESIZE) + GamePanel.TILESIZE;
//                System.out.println(
//                        (((int)ydest / GamePanel.TILESIZE)  * GamePanel.TILESIZE + GamePanel.TILESIZE) + "  " 
//                );
                //System.out.println(ytemp - yy + "\n=========================================================================");

                ytemp = (int) (ytemp - (ytemp - yy));

                //ytemp = (currRow+1) * tileSize;
            } else {
                ytemp += dy;
            }
        }
        if (dy > 0) {
            if (bottomLeft || bottomRight) {
                dy = 0;

                int yy = (int) (((int) (ydest + cheight) / GamePanel.TILESIZE) * GamePanel.TILESIZE);

                //System.out.println(((yy - (ytemp + cheight))) + "=====================================================");
                ytemp = (int) (ytemp + (yy - (ytemp + cheight)));

                //ytemp = (currRow + 1) * GamePanel.TILESIZE - 1;
            } else {
                ytemp += dy;
            }
        }

        //y = ytemp;
        calculateCorners(xdest, ytemp);
        if (dx < 0) {
            if (topLeft || bottomLeft) {
                dx = 0;

                int xx = (int) (((int) xdest / GamePanel.TILESIZE) * GamePanel.TILESIZE) + GamePanel.TILESIZE;

                xtemp = (int) (xtemp - (xtemp - xx));

                //xtemp = currCol * GamePanel.TILESIZE;
            } else {
                xtemp += dx;
            }
        }
        if (dx > 0) {
            if (topRight || bottomRight) {
                dx = 0;

                int xx = (int) (((int) (xdest + cwidth) / GamePanel.TILESIZE) * GamePanel.TILESIZE);

                xtemp = (int) (xtemp + (xx - (xtemp + cwidth)));
                //xtemp = (currCol + 1) * GamePanel.TILESIZE - 1;
            } else {
                xtemp += dx;
            }
        }

        collision(topLeft, topRight, bottomLeft, bottomRight);

        x = xtemp;
        y = ytemp;

//        System.out.println("DEST : " + xdest + " " + ydest);
//        System.out.println("TEMP : " + xtemp + " " + ytemp);
//        System.out.println("X&&Y : " + x + " " + y);
//        System.out.println("-----------------------------------");
    }

    public void calculateCorners(double x, double y) {
        Room room = map.rooms[currRoom.getRow()][currRoom.getCol()];

        int leftTile = (int) ((x) / GamePanel.TILESIZE);
        int rightTile = (int) (x + cwidth - 1) / GamePanel.TILESIZE;
        int topTile = (int) ((y + 1) / GamePanel.TILESIZE);
        int bottomTile = (int) (y + cheight - 1) / GamePanel.TILESIZE;

        //System.out.println(topTile);
        //System.out.println(leftTile);
        topLeft = false;
        topRight = false;
        bottomLeft = false;
        bottomRight = false;

        if (topTile < 0 || bottomTile >= room.intMap.length
                || rightTile >= room.intMap[0].length || leftTile < 0) {
            topLeft = true;
            topRight = true;
            bottomLeft = true;
            bottomRight = true;
            return;
        }

        int tl = room.intMap[topTile][leftTile];
        int tr = room.intMap[topTile][rightTile];
        int bl = room.intMap[bottomTile][leftTile];
        int br = room.intMap[bottomTile][rightTile];

        int i = -1;

        if (leftTile <= 2) {
            i = 3;
        } else if (topTile <= 2) {
            i = 0;
        } else if (rightTile >= 15) {
            i = 1;
        } else if (bottomTile >= 7) {
            i = 2;
        }

        if (!topLeft) {
            topLeft = tl == room.BLOCKED || tl == room.DOOR && room.getLocked(i);
        }
        if (!topRight) {
            topRight = tr == room.BLOCKED || tr == room.DOOR && room.getLocked(i);
        }
        if (!bottomLeft) {
            bottomLeft = bl == room.BLOCKED || bl == room.DOOR && room.getLocked(i);
        }
        if (!bottomRight) {
            bottomRight = br == room.BLOCKED || br == room.DOOR && room.getLocked(i);
        }

        //PIXEL CORRECTION
        if (topLeft && !topRight
                && (x + cwidth) - (rightTile * GamePanel.TILESIZE) >= GamePanel.TILESIZE / 2) {
            pixelCorrectX = true;
        } else if (!topLeft && topRight
                && ((leftTile * GamePanel.TILESIZE) - x) >= -(GamePanel.TILESIZE / 2)) {
            pixelCorrectX = true;
        } else if (bottomLeft && !bottomRight
                && (x + cwidth) - (rightTile * GamePanel.TILESIZE) >= GamePanel.TILESIZE / 2) {
            pixelCorrectX = true;
        } else if (!bottomLeft && bottomRight
                && ((leftTile * GamePanel.TILESIZE) - x) >= -(GamePanel.TILESIZE / 2)) {
            pixelCorrectX = true;
        } else {
            pixelCorrectX = false;
        }

        if (topLeft && !bottomLeft
                && (y + cheight) - (bottomTile * GamePanel.TILESIZE) >= GamePanel.TILESIZE / 2) {
            pixelCorrectY = true;
        } else if (!topLeft && bottomLeft
                && ((topTile * GamePanel.TILESIZE) - y) >= -(GamePanel.TILESIZE / 2)) {
            pixelCorrectY = true;
        } else if (topRight && !bottomRight
                && (y + cheight) - (bottomTile * GamePanel.TILESIZE) >= GamePanel.TILESIZE / 2) {
            pixelCorrectY = true;
        } else if (!topRight && bottomRight
                && ((topTile * GamePanel.TILESIZE) - y) >= -(GamePanel.TILESIZE / 2)) {
            pixelCorrectY = true;
        } else {
            pixelCorrectY = false;
        }

        checkEntityCollision(x, y);
    }

    public void checkEntityCollision(double x, double y) {

    }

    public void calcDirection() {

    }

    public void attack(int direction) {
        if (!attacking
                && System.nanoTime() - lastAttack >= timeBetweenAttacks) {
            attacking = true;
            lastAttack = System.nanoTime();
            Audio.ATTACK.stop();
            Audio.ATTACK.play(false);
        }

        if (System.nanoTime() - lastAttack >= attackDuration) {
            attacking = false;
        }
    }

    public void flinch() {
        if (flinching) {

            if (lastFlinch + flinchLength <= System.nanoTime()) {
                flinching = false;
            }

            if (lastBlink + timeBetweenBlinks <= System.nanoTime()) {
                lastBlink = System.nanoTime();
                blinking = !blinking;
            }

        }
    }

    @Override
    public void update() {
        if (!map.transition) {
            //getNextPosition(6 * Math.round(dt));

            checkTileMapCollision();
            setPosition(xtemp, ytemp);
        }
    }
    
    @Override
    public void update(double deltaTime) {
        if (!map.transition) {
            //getNextPosition(6 * Math.round(dt));

            checkTileMapCollision();
            setPosition(xtemp, ytemp);
        }
    }

    @Override
    public void render(Graphics2D g) {
//        super.render(g);

        if (blinking && flinching) {

            if (animations[currAnimation] != null && !map.transition) {

                if (animations[currAnimation].getWidth() > width || animations[currAnimation].getHeight() > height) {
                    g.drawImage(animations[currAnimation].getSprite(), (int) (Map.xdis + x + (cwidth - animations[currAnimation].getWidth()) / 2), (int) (Map.ydis + y + (cheight - animations[currAnimation].getHeight()) / 2),
                            animations[currAnimation].getWidth(), animations[currAnimation].getHeight(), null);
                } else {
                    g.drawImage(animations[currAnimation].getSprite(), (int) (Map.xdis + x + (cwidth - width) / 2), (int) (Map.ydis + y + (cheight - height)),
                            animations[currAnimation].getWidth(), animations[currAnimation].getHeight(), null);
                }

            }
        }

        if (!flinching) {
            if (animations[currAnimation] != null && !map.transition) {

                if (animations[currAnimation].getWidth() > width || animations[currAnimation].getHeight() > height) {
                    g.drawImage(animations[currAnimation].getSprite(), (int) (Map.xdis + x + (cwidth - animations[currAnimation].getWidth()) / 2), (int) (Map.ydis + y + (cheight - animations[currAnimation].getHeight()) / 2),
                            animations[currAnimation].getWidth(), animations[currAnimation].getHeight(), null);
                } else {
                    g.drawImage(animations[currAnimation].getSprite(), (int) (Map.xdis + x + (cwidth - width) / 2), (int) (Map.ydis + y + (cheight - height)),
                            animations[currAnimation].getWidth(), animations[currAnimation].getHeight(), null);
                }
            }
        }
    }
}
