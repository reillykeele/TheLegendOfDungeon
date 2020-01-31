/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Entity.Enemies.Enemy;
import Main.Audio;
import Main.AudioPlayer;
//import Main.ControllerHandler;
import Main.GamePanel;
import Main.InputManager;
import Main.KeyHandler;
import World.Map;
import World.Room;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import static java.awt.event.KeyEvent.*;

public class Player extends Entity {

    private static final int NIDLE = 0;
    private static final int EIDLE = 1;
    private static final int SIDLE = 2;
    private static final int WIDLE = 3;

    private static final int NWALK = 4;
    private static final int EWALK = 5;
    private static final int SWALK = 6;
    private static final int WWALK = 7;

    private static final int NATTACK = 8;
    private static final int EATTACK = 9;
    private static final int SATTACK = 10;
    private static final int WATTACK = 11;

    //GET ENEMIES FROM ARRAYLIST OF CURRENT ROOM, CHECK WITH RECTANGLE BOUNDING
    //BOXES IN UPDATE OR CHECKENTITY()
    private HUD hud;

    private int coinQueue = 0;
    private int coins = 0;
    private boolean bossKey = false;

    private boolean dead;

    public Player(Map map, double x, double y) {
        super(map, x, y);

        width = 32;
        height = 48;

        cwidth = GamePanel.TILESIZE;
        cheight = GamePanel.TILESIZE;

        health = 5;
        speed = 4;
        //4

        damage = 1;

        facing = SOUTH;

        timeBetweenAttacks = (long) (1000000000 * 0.35);
        attackDuration = (long) (1000000000 * 0.17);

        animations = new Animation[12];

        try {

            //public Animation(String file, long frameLength, int numFrames, int width, int height, boolean animated, boolean loop) {
            //animations[0] = new Animation("src/Resources/Images/Sprites/Player/playerWalkSouthSheet.gif", 100000000, 8, 16, 24, true, true);
//            animations[NIDLE] = new Animation("src/Resources/Images/Sprites/Player/playerIdleNorth.gif", (long) 50000000, 1, 32, 48, false, false);
//            animations[EIDLE] = new Animation("src/Resources/Images/Sprites/Player/playerIdleEast.gif", (long) 50000000, 1, 32, 48, false, false);
//            animations[SIDLE] = new Animation("src/Resources/Images/Sprites/Player/playerIdleSouth.gif", (long) 50000000, 1, 32, 48, false, false);
//            animations[WIDLE] = new Animation("src/Resources/Images/Sprites/Player/playerIdleWest.gif", (long) 50000000, 1, 32, 48, false, false);
//
//            animations[NWALK] = new Animation("src/Resources/Images/Sprites/Player/playerWalkNorthSheet.gif", (long) 50000000, 8, 32, 48, true, true);
//            animations[EWALK] = new Animation("src/Resources/Images/Sprites/Player/playerWalkEastSheet.gif", (long) 50000000, 8, 32, 48, true, true);
//            animations[SWALK] = new Animation("src/Resources/Images/Sprites/Player/playerWalkSouthSheet.gif", (long) 50000000, 8, 32, 48, true, true);
//            animations[WWALK] = new Animation("src/Resources/Images/Sprites/Player/playerWalkWestSheet.gif", (long) 50000000, 8, 32, 48, true, true);
//
//            animations[NATTACK] = new Animation("src/Resources/Images/Sprites/Player/nAttackSheet.gif", (long) (attackDuration / 8), 6, 80, 86, true, false);
//            animations[EATTACK] = new Animation("src/Resources/Images/Sprites/Player/eAttackSheet.gif", (long) (attackDuration / 8), 6, 64, 64, true, false);
//            animations[SATTACK] = new Animation("src/Resources/Images/Sprites/Player/sAttackSheet.gif", (long) (attackDuration / 6), 5, 76, 64, true, false);
//            animations[WATTACK] = new Animation("src/Resources/Images/Sprites/Player/wAttackSheet.gif", (long) (attackDuration / 8), 6, 64, 64, true, false);
            animations[NIDLE] = new Animation("/Resources/Images/Sprites/Player/idle/idleN.gif", (long) 50000000, 1, 112, 112, false, false);
            animations[EIDLE] = new Animation("/Resources/Images/Sprites/Player/idle/idleE.gif", (long) 50000000, 1, 112, 112, false, false);
            animations[SIDLE] = new Animation("/Resources/Images/Sprites/Player/idle/idleS.gif", (long) 50000000, 1, 112, 112, false, false);
            animations[WIDLE] = new Animation("/Resources/Images/Sprites/Player/idle/idleW.gif", (long) 50000000, 1, 112, 112, false, false);

            animations[NWALK] = new Animation("/Resources/Images/Sprites/Player/walk/walkN.gif", (long) 50000000, 10, 112, 112, true, true);
            animations[EWALK] = new Animation("/Resources/Images/Sprites/Player/walk/walkE.gif", (long) 50000000, 8, 112, 112, true, true);
            animations[SWALK] = new Animation("/Resources/Images/Sprites/Player/walk/walkS.gif", (long) 50000000, 8, 112, 112, true, true);
            animations[WWALK] = new Animation("/Resources/Images/Sprites/Player/walk/walkW.gif", (long) 50000000, 8, 112, 112, true, true);

            animations[NATTACK] = new Animation("/Resources/Images/Sprites/Player/attack/attackN.gif", (long) (attackDuration / 8), 6, 112, 112, true, false);
            animations[EATTACK] = new Animation("/Resources/Images/Sprites/Player/attack/attackE.gif", (long) (attackDuration / 8), 6, 112, 112, true, false);
            animations[SATTACK] = new Animation("/Resources/Images/Sprites/Player/attack/attackS.gif", (long) (attackDuration / 6), 5, 112, 112, true, false);
            animations[WATTACK] = new Animation("/Resources/Images/Sprites/Player/attack/attackW.gif", (long) (attackDuration / 8), 6, 112, 112, true, false);

            currAnimation = SIDLE;

        } catch (Exception e) {

        }

    }

    public boolean getDead() {
        return dead;
    }

    public int getCoins() {
        return coins;
    }

    public boolean getBossKey() {
        return bossKey;
    }

    @Override
    public void getNextPosition(double speed) {
        if (left && !right) {
            if (dx > -speed) {
                dx -= speed;
            } else {
                dx = -speed;
            }
            //x += dx;
        } else if (right && !left) {
            if (dx < speed) {
                dx += speed;
            } else {
                dx = speed;
            }
            //x += dx;
        } else {
            if (dx > 0) {
                dx -= speed;
                if (dx < 0) {
                    dx = 0;
                }
            }
            if (dx < 0) {
                dx += speed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        if (up && !down) {
            if (dy > -speed) {
                dy -= speed;
            } else {
                dy = -speed;
            }
            y += dy;
        } else if (down && !up) {
            if (dy < speed) {
                dy += speed;
            } else {
                dy = speed;
            }
            //y += dy;
        } else {
            if (dy < 0) {
                dy += speed;
                if (dy > 0) {
                    dy = 0;
                }
            }
            if (dy > 0) {
                dy -= speed;
                if (dy < 0) {
                    dy = 0;
                }
            }

        }
    }

    @Override
    public void checkEntityCollision(double x, double y) {
        for (Enemy e : map.getRoom(currRoom.getRow(), currRoom.getCol()).getEnemies()) {
            if (getRect((int) x, (int) y).intersects(e.getRect((int) e.getX(), (int) e.getY()))) {

                if (x >= e.getX() && y >= e.getY()) {
                    topLeft = true;
                }
                if (x >= e.getX() && y <= e.getY() + e.getCheight()) {
                    bottomLeft = true;
                }
                if (x + cwidth >= e.getX() && y >= e.getY()) {
                    topRight = true;
                }
                if (x + cwidth >= e.getX() && y <= e.getY()) {
                    bottomRight = true;
                }

                if (!flinching && !e.getFlinching()) {
                    Audio.PLAYERHIT.play(false);

                    setFlinching(true);
                    setHealth(getHealth() - e.getDamage());
                }
            }
        }

        if (map.getRoom(currRoom.getRow(), currRoom.getCol()).getChest() != null) {
            Chest chest = map.getRoom(currRoom.getRow(), currRoom.getCol()).getChest();
            if (getRect((int) x, (int) y).intersects(chest.getRect((int) chest.getX(), (int) chest.getY()))) {
                if (x >= chest.getX() && y >= chest.getY()) {
                    topLeft = true;
                }
                if (x >= chest.getX() && y <= chest.getY() + chest.getCheight()) {
                    bottomLeft = true;
                }
                if (x + cwidth >= chest.getX() && y >= chest.getY()) {
                    topRight = true;
                }
                if (x + cwidth >= chest.getX() && y <= chest.getY()) {
                    bottomRight = true;
                }
            }
        }
    }

    @Override
    public void calcDirection() {
        if (left) {
            if (left && right) {

            } else if (!up && !down) {
                facing = WEST;
            } else if (facing == EAST && !right) {
                facing = WEST;
            }

        }
        if (right) {
            if (left && right) {

            } else if (!up && !down) {
                facing = EAST;
            } else if (facing == WEST && !left) {
                facing = EAST;
            }
        }

        if (down) {
            if (up && down) {

            } else if (!right && !left) {
                facing = SOUTH;
            } else if (facing == NORTH && !up) {
                facing = SOUTH;
            }
        }

        if (up) {
            if (up && down) {

            } else if (!right && !left) {
                facing = NORTH;
            } else if (facing == SOUTH && !down) {
                facing = NORTH;
            }
        }
    }

    private Rectangle rect;

    @Override
    public void attack(int direction
    ) {
        super.attack(direction);

//        Rectangle rect;
        switch (direction) {
            case NORTH:
                rect = new Rectangle((int) (x - (cwidth / 2)), (int) (y - cheight), cwidth * 2, cheight);
                break;
            case EAST:
                rect = new Rectangle((int) (x + (cwidth)), (int) (y - (cheight / 2)), cwidth, cheight * 2);
                break;
            case SOUTH:
                rect = new Rectangle((int) (x - (cwidth / 2)), (int) (y + cheight), cwidth * 2, cheight);
                break;
            case WEST:
                rect = new Rectangle((int) (x - (cwidth)), (int) (y - (cheight / 2)), cwidth, cheight * 2);
                break;
            default:
                rect = null;
                break;
        }

        for (Enemy e : map.getRoom(currRoom.getRow(), currRoom.getCol()).getEnemies()) {
            if (rect.intersects(e.getRect((int) e.getX(), (int) e.getY()))) {
                //hit enemy
                if (!e.getFlinching()) {
                    Audio.ENEMYHIT.play(false);

                    e.setFlinching(true);
                    e.setHealth(e.getHealth() - damage);
                }
            }
        }

        if (map.getRoom(currRoom.getRow(), currRoom.getCol()).getChest() != null) {
            Chest chest = map.getRoom(currRoom.getRow(), currRoom.getCol()).getChest();

            if (rect.intersects(chest.getRect((int) chest.getX(), (int) chest.getY()))) {

                if (chest.getOpen() == false) {
                    switch (chest.getItem()) {
                        case Chest.BOSSKEY:
                            bossKey = true;
                            Audio.GETITEM.play(false);

                            map.setHasKey(true);

                            break;
                        case Chest.COINS:
                            coinQueue += 50;
                            Audio.GETCOIN2.play(true);

                            break;
                    }
                }

                chest.setOpen(true);

            }
        }

    }

    public void calcAnimation() {

        int prevAnimation = currAnimation;

        switch (facing) {
            case NORTH:

                if (attacking) {
                    currAnimation = NATTACK;

                } else if (up) {
                    currAnimation = NWALK;
                } else {
                    currAnimation = NIDLE;
                }

                break;

            case EAST:
                if (attacking) {
                    currAnimation = EATTACK;
                } else if (right) {
                    currAnimation = EWALK;
                } else {
                    currAnimation = EIDLE;
                }
                break;
            case SOUTH:
                if (attacking) {
                    currAnimation = SATTACK;
                } else if (down) {
                    currAnimation = SWALK;
                } else {
                    currAnimation = SIDLE;
                }
                break;
            case WEST:
                if (attacking) {
                    currAnimation = WATTACK;
                } else if (left) {
                    currAnimation = WWALK;
                } else {
                    currAnimation = WIDLE;
                }
                break;
        }

        if (prevAnimation != currAnimation) {
            animations[prevAnimation].stop();
            animations[currAnimation].start();
        }

    }

    //Room room = map.rooms[currRoom.getRow()][currRoom.getCol()];
    @Override
    public void update() {

        calcAnimation();
        animate();

        //ATTACK
        if (!map.transition) {

            if (coinQueue > 0) {
                coins++;
                coinQueue--;

                if (coinQueue == 1) {
                    Audio.GETCOIN2.stop();
                    Audio.GETCOIN.play(false);
                }
            }

            if (attack || attacking) {
                attack(facing);
                dx = 0;
                dy = 0;
            }

            if (!attacking) {
                calcDirection();

                if (!attack) {
                    getNextPosition(speed);
                }
            }

            if (flinching) {
                flinch();
            }

            checkTileMapCollision();
            setPosition(xtemp, ytemp);

            hud = new HUD(this);

            if (getHealth() <= 0) {
                dead = true;
            }

            //CHECK DOORS
            //if (!map.rooms[currRoom.getRow()][currRoom.getCol()].getLocked()) {
            if (y / GamePanel.TILESIZE == 1) {
                map.nextRoom(Map.NORTH);
                setPosition(x, 8 * GamePanel.TILESIZE);
            }

            if ((int) (x + 1) / GamePanel.TILESIZE == 15) {
                map.nextRoom(Map.EAST);
                setPosition(2 * GamePanel.TILESIZE, y);
            }

            if ((int) (y + 1) / GamePanel.TILESIZE == 9) {
                map.nextRoom(Map.SOUTH);

                setPosition(x, 2 * GamePanel.TILESIZE);
            }

            if (x / GamePanel.TILESIZE == 1) {
                map.nextRoom(Map.WEST);
                setPosition(14 * GamePanel.TILESIZE, y);
            }
            //}
        }
    }

    @Override
    public void render(Graphics2D g
    ) {
        super.render(g);

        if (!map.transition) {

            //if attacking
//            if (attacking) {
//                g.setColor(Color.RED);
//
//                Rectangle rect;
//                switch (facing) {
//                    case NORTH:
//                        rect = new Rectangle(Map.xdis + (int) (x - (cwidth / 2)), Map.ydis + (int) (y - cheight), cwidth * 2, cheight);
//                        break;
//                    case EAST:
//                        rect = new Rectangle(Map.xdis + (int) (x + (cwidth)), Map.ydis + (int) (y - (cheight / 2)), cwidth, cheight * 2);
//                        break;
//                    case SOUTH:
//                        rect = new Rectangle(Map.xdis + (int) (x - (cwidth / 2)), Map.ydis + (int) (y + cheight), cwidth * 2, cheight);
//                        break;
//                    case WEST:
//                        rect = new Rectangle(Map.xdis + (int) (x - (cwidth)), Map.ydis + (int) (y - (cheight / 2)), cwidth, cheight * 2);
//                        break;
//                    default:
//                        rect = null;
//                        break;
//                }
//                g.fill(rect);
//            }
            //draw player
        }

        if (hud != null) {
            hud.render(g);
        }
    }

    public void KeyPressed(int k) {

//        if (k == VK_A) {
//            attack = true;
//        }
//
//        if (k == VK_UP) {
//            up = true;
//        }
//        if (k == VK_DOWN) {
//            down = true;
//        }
//        if (k == VK_RIGHT) {
//            right = true;
//        }
//        if (k == VK_LEFT) {
//            left = true;
//        }
    }

    public void keyReleased(int k) {
//
//        System.out.println("KEY RELEASED");
//
//        if (k == VK_A) {
//            attack = false;
//        }
//
//        if (k == VK_UP) {
//            up = false;
//        }
//        if (k == VK_DOWN) {
//            down = false;
//        }
//        if (k == VK_RIGHT) {
//            right = false;
//        }
//        if (k == VK_LEFT) {
//            left = false;
//        }
    }

    public void processInput() {

        //System.out.println(KeyHandler.keyState[KeyHandler.BUTTON1]);
//        attack = input.isKeyDown(VK_A);
//        up = input.isKeyDown(VK_UP);
//        down = input.isKeyDown(VK_DOWN);
//        right = input.isKeyDown(VK_RIGHT);
//        left = input.isKeyDown(VK_LEFT);
//        if (KeyHandler.isPressed(KeyHandler.BUTTON1)) {
//            attack(facing);
//        }
//        if (ControllerHandler.isHeld(ControllerHandler.L2)) {
//            damage = 3;
//        } else {
//            damage = 1;
//        }
//
//        if (ControllerHandler.isHeld(ControllerHandler.R2)) {
//            speed = 8;
//        } else {
//            speed = 4;
//        }

        damage = 1;
        speed = 4;

//        attack = (KeyHandler.keyState[KeyHandler.BUTTON1]) || ControllerHandler.isHeld(ControllerHandler.SQUARE);
//        up = KeyHandler.keyState[KeyHandler.UP] || ControllerHandler.getDpadHeld(ControllerHandler.UP);
//        down = KeyHandler.keyState[KeyHandler.DOWN] || ControllerHandler.getDpadHeld(ControllerHandler.DOWN);
//        right = KeyHandler.keyState[KeyHandler.RIGHT] || ControllerHandler.getDpadHeld(ControllerHandler.RIGHT);
//        left = KeyHandler.keyState[KeyHandler.LEFT] || ControllerHandler.getDpadHeld(ControllerHandler.LEFT);

        attack = (KeyHandler.keyState[KeyHandler.BUTTON1]) ;
        up = KeyHandler.keyState[KeyHandler.UP] ;
        down = KeyHandler.keyState[KeyHandler.DOWN] ;
        right = KeyHandler.keyState[KeyHandler.RIGHT];
        left = KeyHandler.keyState[KeyHandler.LEFT] ;
    }
}
