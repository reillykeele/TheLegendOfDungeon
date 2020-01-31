/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entity.Chest;
import Entity.Enemies.Bat;
import Entity.Enemies.Enemy;
import Main.Audio;
import Main.GamePanel;
import StateMachine.MenuState;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Reilly
 */
public class Room {

    private static BufferedImage imgDoorN;
    private static BufferedImage imgDoorE;
    private static BufferedImage imgDoorS;
    private static BufferedImage imgDoorW;

    private static BufferedImage imgLockedN;
    private static BufferedImage imgLockedE;
    private static BufferedImage imgLockedS;
    private static BufferedImage imgLockedW;

    private static BufferedImage imgLockedDoorN;
    private static BufferedImage imgLockedDoorE;
    private static BufferedImage imgLockedDoorS;
    private static BufferedImage imgLockedDoorW;

    private static BufferedImage imgUnlockedDoorN;
    private static BufferedImage imgUnlockedDoorE;
    private static BufferedImage imgUnlockedDoorS;
    private static BufferedImage imgUnlockedDoorW;

    private static BufferedImage imgRoom;
    private static BufferedImage imgChestOpen;
    private static BufferedImage imgChestClosed;

    public static final int PASSABLE = 0;
    public static final int BLOCKED = 1;
    public static final int DOOR = 2;

    //ROOM TYPE
    public static final int DEFAULT = 0;
    public static final int SPAWN = 1;
    public static final int TREASURE = 2;
    public static final int BOSS = 3;
    public static final int BOSSKEY = 4;

    public static final int COINS = 5;

    private Chest chest;

    // DEFAULT ROOMS SPAWNS ENEMIES. MUST ELIMINATE ALL ENEMIES FOR DOORS TO UNLOCK.
    // SPAWN ROOM IS EMPTY. 
    // TREASURE ROOMS SPAWN ONE CHEST IN THE CENTER OF THE ROOM. 
    // BOSS ROOM LOCKS DOORS AND SPAWNS BOSS. 
    private int roomType = -1;

    private static final Color COLOR_WALL = new Color(66, 91, 121, 255);
    private static final Color COLOR_FLOOR = new Color(103, 145, 137, 255);
    private static final Color COLOR_DOOR = new Color(63, 54, 86, 255);

    //PICK A RANDOM NUMBER OF ENEMIES TO SPAWN IN ROOM IF ROOM != TREASURE / BOSS / SPAWN. 
    //LOCK ALL DOORS WITH BOOLEAN UNTIL ENEMIES ARE DEFEATED. 
    private ArrayList<Enemy> enemies;

    private Map map;

    private RoomTemplateType roomTemplateType;
    public boolean[] exits;

    public boolean[] lockedExits;   

    public int[][] intMap;

    private Vector2 location;

    //15 x 9 ROOM, UP TO 4 EXITS
    //OUTER RIM IS NOT PASSABLE THEREFOR
    //13 X 7
    public Room(Map map, int r, int c) {
        this.map = map;
        exits = new boolean[4];
        lockedExits = new boolean[4];
        //lockedExits[0] = true;

        location = new Vector2(r, c);

        try {
            
            //getClass().getResourcesAsStream
            imgDoorN = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/doorN.gif"));
            imgDoorE = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/doorE.gif"));
            imgDoorS = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/doorS.gif"));
            imgDoorW = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/doorW.gif"));

            imgLockedN = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/lockedN.gif"));
            imgLockedE = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/lockedE.gif"));
            imgLockedS = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/lockedS.gif"));
            imgLockedW = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/lockedW.gif"));

            imgLockedDoorN = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/lockedDoorN.gif"));
            imgLockedDoorE = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/lockedDoorE.gif"));
            imgLockedDoorS = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/lockedDoorS.gif"));
            imgLockedDoorW = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/lockedDoorW.gif"));

            imgUnlockedDoorN = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/unlockedDoorN.gif"));
            imgUnlockedDoorE = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/unlockedDoorE.gif"));
            imgUnlockedDoorS = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/unlockedDoorS.gif"));
            imgUnlockedDoorW = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/unlockedDoorW.gif"));

            imgRoom = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Room/room.gif"));
            imgChestOpen = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Items/chestOpen.gif"));
            imgChestClosed = ImageIO.read(getClass().getResourceAsStream("/Resources/Images/Items/chestClosed.gif"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        enemies = new ArrayList<Enemy>();
    }

    public void addExit(int direction) {
        switch (direction) {
            case Map.NORTH:
                exits[Map.NORTH] = true;
                break;
            case Map.EAST:
                exits[Map.EAST] = true;
                break;
            case Map.SOUTH:
                exits[Map.SOUTH] = true;
                break;
            case Map.WEST:
                exits[Map.WEST] = true;
                break;
        }

        calcRoomTemplateType();
    }

    public void setRoomType(int i) {
        roomType = i;

        if (roomType == TREASURE) {
            spawnChest(Chest.COINS);
            //spawn chest
            //intMap[Map.ROWS / 2][Map.COLS / 2] = BLOCKED;
            //add coins
        }

        if (roomType == BOSSKEY) {
            spawnChest(Chest.BOSSKEY);
            //intMap[Map.ROWS / 2][Map.COLS / 2] = BLOCKED;
            //add boss key item
        }

        if (roomType == BOSS) {
            for (int j = 0; j < 4; j++) {
                if (exits[j] == true) {
                    switch (j) {
                        case Map.NORTH:
                            map.rooms[location.getRow() - 1][location.getCol()].lockedExits[Map.SOUTH] = true;
                            break;
                        case Map.EAST:
                            map.rooms[location.getRow()][location.getCol() + 1].lockedExits[Map.WEST] = true;
                            break;
                        case Map.SOUTH:
                            map.rooms[location.getRow() + 1][location.getCol()].lockedExits[Map.NORTH] = true;
                            break;
                        case Map.WEST:
                            map.rooms[location.getRow()][location.getCol() - 1].lockedExits[Map.EAST] = true;
                            break;
                    }

                    return;
                }
            }

            if (roomType == SPAWN) {
                intMap[Map.ROWS / 2][Map.COLS / 2] = PASSABLE;
                //add coins
            }

        }
    }

    public void spawnChest(int i) {

        if (chest != null) {
            return;
        }

        chest = new Chest(map, (Map.COLS / 2) * GamePanel.TILESIZE, (Map.ROWS / 2) * GamePanel.TILESIZE, i);

    }

    public Chest getChest() {
        return chest;
    }

    public int getRoomType() {
        return roomType;
    }

    public void calcRoomTemplateType() {
        //4 C 4
        if (exits[Map.NORTH] && exits[Map.EAST] && exits[Map.SOUTH] && exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.NORTH_EAST_SOUTH_WEST;
        }
        //4 C 1
        if (exits[Map.NORTH] && !exits[Map.EAST] && !exits[Map.SOUTH] && !exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.NORTH;
        }
        if (!exits[Map.NORTH] && exits[Map.EAST] && !exits[Map.SOUTH] && !exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.EAST;
        }
        if (!exits[Map.NORTH] && !exits[Map.EAST] && exits[Map.SOUTH] && !exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.SOUTH;
        }
        if (!exits[Map.NORTH] && !exits[Map.EAST] && !exits[Map.SOUTH] && exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.WEST;
        }
        //4 C 2
        if (exits[Map.NORTH] && exits[Map.EAST] && !exits[Map.SOUTH] && !exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.NORTH_EAST;
        }
        if (exits[Map.NORTH] && !exits[Map.EAST] && exits[Map.SOUTH] && !exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.NORTH_SOUTH;
        }
        if (exits[Map.NORTH] && !exits[Map.EAST] && !exits[Map.SOUTH] && exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.NORTH_WEST;
        }
        if (!exits[Map.NORTH] && exits[Map.EAST] && exits[Map.SOUTH] && !exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.EAST_SOUTH;
        }
        if (!exits[Map.NORTH] && exits[Map.EAST] && !exits[Map.SOUTH] && exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.EAST_WEST;
        }
        if (!exits[Map.NORTH] && !exits[Map.EAST] && exits[Map.SOUTH] && exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.SOUTH_WEST;
        }
        //4 C 3
        if (exits[Map.NORTH] && exits[Map.EAST] && exits[Map.SOUTH] && !exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.NORTH_EAST_SOUTH;
        }
        if (exits[Map.NORTH] && exits[Map.EAST] && !exits[Map.SOUTH] && exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.NORTH_EAST_WEST;
        }
        if (exits[Map.NORTH] && !exits[Map.EAST] && exits[Map.SOUTH] && exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.NORTH_SOUTH_WEST;
        }
        if (!exits[Map.NORTH] && exits[Map.EAST] && exits[Map.SOUTH] && exits[Map.WEST]) {
            roomTemplateType = roomTemplateType.EAST_SOUTH_WEST;
        }

        intMap = map.getRoomTemplate(roomTemplateType.getIndex());
    }

    //checks if there is an exit in direction d
    public boolean hasExit(int d) {
        return exits[d];
    }

    public boolean[] getExitArray() {
        return exits;
    }

    public int getNumExits() {
        int n = 0;
        for (int i = 0; i < exits.length; i++) {
            if (exits[i]) {
                n++;
            }
        }
        return n;
    }

    public ArrayList<Enemy> getEnemy() {
        return enemies;
    }        

    public boolean getLocked(int direction) {
        return lockedExits[direction];
    }

//    public void unlockDoors() {
//        for (int i = 0; i < 4; i++) {
//
//            if (hasExit(i)) {
//                switch (i) {
//                    case Map.NORTH:
//                        if (map.getRoom(location.getRow() - 1, location.getCol()).getRoomType() != BOSS) {
//                            lockedExits[i] = false;
//                        }
//                        break;
//                    case Map.EAST:
//                        if (map.getRoom(location.getRow(), location.getCol() + 1).getRoomType() != BOSS) {
//                            lockedExits[i] = false;
//                        }
//                        break;
//                    case Map.SOUTH:
//                        if (map.getRoom(location.getRow() + 1, location.getCol()).getRoomType() != BOSS) {
//                            lockedExits[i] = false;
//                        }
//                        break;
//                    case Map.WEST:
//                        if (map.getRoom(location.getRow(), location.getCol() - 1).getRoomType() != BOSS) {
//                            lockedExits[i] = false;
//                        }
//                        break;
//                }
//            }
//            //lockedExits[i] = false;
//        }
//    }

    public void unlockDoors() {
        for (int i = 0; i < 4; i++) {

            if (hasExit(i)) {
                switch (i) {
                    case Map.NORTH:
                        if (map.getRoom(location.getRow() - 1, location.getCol()).getRoomType() != BOSS || (map.getRoom(location.getRow() - 1, location.getCol()).getRoomType() == BOSS && map.playerHasKey())) {
                            lockedExits[i] = false;
                        }
                        break;
                    case Map.EAST:
                        if (map.getRoom(location.getRow(), location.getCol() + 1).getRoomType() != BOSS || (map.getRoom(location.getRow(), location.getCol() + 1).getRoomType() == BOSS && map.playerHasKey())) {
                            lockedExits[i] = false;
                        }
                        break;
                    case Map.SOUTH:
                        if (map.getRoom(location.getRow() + 1, location.getCol()).getRoomType() != BOSS || (map.getRoom(location.getRow() + 1, location.getCol()).getRoomType() == BOSS && map.playerHasKey())) {
                            lockedExits[i] = false;
                        }
                        break;
                    case Map.WEST:
                        if (map.getRoom(location.getRow(), location.getCol() - 1).getRoomType() != BOSS || (map.getRoom(location.getRow(), location.getCol() - 1).getRoomType() == BOSS && map.playerHasKey())) {
                            lockedExits[i] = false;
                        }
                        break;
                }
            }
        }

    }

    public void generateEnemies() {
        int numEnemies = (int) (Math.random() * 4) + 1;

        for (int i = 0; i < numEnemies; i++) {
            int r;
            int c;

            r = (int) (Math.random() * (7 - 4) + 4);
            c = (int) (Math.random() * (11 - 4) + 4);

            //System.out.println(r * 32);
            enemies.add(new Bat(map, c * GamePanel.TILESIZE, r * GamePanel.TILESIZE));

            if (enemies.size() > 0) {
                for (int j = 0; j < 4; j++) {
                    if (hasExit(j)) {
                        lockedExits[j] = true;
                    }
                }
            }

        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void update() {

//        for (Enemy e : enemies) {
//            e.update();
//                        
//        }
        int i = 0;
        while (i < enemies.size()) {
            enemies.get(i).update();

            if (enemies.get(i).getHealth() <= 0) {
                enemies.remove(i);
                Audio.ENEMYDIE.play(false);
            } else {
                i++;
            }
        }

        if (enemies.size() <= 0) {            
            unlockDoors();
        }
    }

    public void render(Graphics2D g) {
        //render room
        for (int r = 0; r < intMap.length; r++) {
            for (int c = 0; c < intMap[0].length; c++) {
                switch (intMap[r][c]) {
                    case 0:
                        g.setColor(COLOR_FLOOR);
                        break;
                    case 1:
                        g.setColor(COLOR_WALL);
                        break;
                    case 2:
                        g.setColor(COLOR_DOOR);
                        break;
                }

                //g.fillRect(c * 32 +  150, r * 32 + 50, 32, 32);
                g.fillRect(Map.xdis + c * 32, Map.ydis + r * 32, 32, 32);
            }
        }
        //DRAW ROOM
        g.drawImage(imgRoom, Map.xdis, Map.ydis, imgRoom.getWidth() * 2, imgRoom.getHeight() * 2, null);

        //DRAW DOORS
        if (exits[Map.NORTH]) {
            //if boss door
            if (map.getRoom(location.getRow() - 1, location.getCol()).getRoomType() == BOSS) {
                //if boss door locked
                if (lockedExits[Map.NORTH] == true) {
                    g.drawImage(imgLockedDoorN, Map.xdis + (7 * GamePanel.TILESIZE), Map.ydis, imgDoorN.getWidth() * 2, imgDoorN.getHeight() * 2, null);
                } else {
                    //System.out.println("unlocked door n");
                    g.drawImage(imgUnlockedDoorN, Map.xdis + (7 * GamePanel.TILESIZE), Map.ydis, imgDoorN.getWidth() * 2, imgDoorN.getHeight() * 2, null);

                }
                //if door is locked
            } else if (lockedExits[Map.NORTH] == true) {
                g.drawImage(imgLockedN, Map.xdis + (7 * GamePanel.TILESIZE), Map.ydis, imgDoorN.getWidth() * 2, imgDoorN.getHeight() * 2, null);
            } else {
                g.drawImage(imgDoorN, Map.xdis + (7 * GamePanel.TILESIZE), Map.ydis, imgDoorN.getWidth() * 2, imgDoorN.getHeight() * 2, null);
            }
        }
        if (exits[Map.EAST]) {
            if (map.getRoom(location.getRow(), location.getCol() + 1).getRoomType() == BOSS) {
                if (lockedExits[Map.EAST] == true) {
                    g.drawImage(imgLockedDoorE, Map.xdis + (Map.COLS - 2) * GamePanel.TILESIZE, Map.ydis + (4 * GamePanel.TILESIZE), imgDoorE.getWidth() * 2, imgDoorE.getHeight() * 2, null);
                } else {
                    g.drawImage(imgUnlockedDoorE, Map.xdis + (Map.COLS - 2) * GamePanel.TILESIZE, Map.ydis + (4 * GamePanel.TILESIZE), imgDoorE.getWidth() * 2, imgDoorE.getHeight() * 2, null);
                }
            } else if (lockedExits[Map.EAST] == true) {
                g.drawImage(imgLockedE, Map.xdis + (Map.COLS - 2) * GamePanel.TILESIZE, Map.ydis + (4 * GamePanel.TILESIZE), imgDoorE.getWidth() * 2, imgDoorE.getHeight() * 2, null);
            } else {
                g.drawImage(imgDoorE, Map.xdis + (Map.COLS - 2) * GamePanel.TILESIZE, Map.ydis + (4 * GamePanel.TILESIZE), imgDoorE.getWidth() * 2, imgDoorE.getHeight() * 2, null);
            }
        }
        if (exits[Map.SOUTH]) {
            if (map.getRoom(location.getRow() + 1, location.getCol()).getRoomType() == BOSS) {
                if (lockedExits[Map.SOUTH] == true) {
                    g.drawImage(imgLockedDoorS, Map.xdis + (7 * GamePanel.TILESIZE), Map.ydis + (Map.ROWS - 2) * GamePanel.TILESIZE, imgDoorS.getWidth() * 2, imgDoorS.getHeight() * 2, null);
                } else {
                    g.drawImage(imgUnlockedDoorS, Map.xdis + (7 * GamePanel.TILESIZE), Map.ydis + (Map.ROWS - 2) * GamePanel.TILESIZE, imgDoorS.getWidth() * 2, imgDoorS.getHeight() * 2, null);
                }
            } else if (lockedExits[Map.SOUTH] == true) {
                g.drawImage(imgLockedS, Map.xdis + (7 * GamePanel.TILESIZE), Map.ydis + (Map.ROWS - 2) * GamePanel.TILESIZE, imgDoorS.getWidth() * 2, imgDoorS.getHeight() * 2, null);
            } else {
                g.drawImage(imgDoorS, Map.xdis + (7 * GamePanel.TILESIZE), Map.ydis + (Map.ROWS - 2) * GamePanel.TILESIZE, imgDoorS.getWidth() * 2, imgDoorS.getHeight() * 2, null);
            }
        }
        if (exits[Map.WEST]) {
            if (map.getRoom(location.getRow(), location.getCol() - 1).getRoomType() == BOSS) {
                if (lockedExits[Map.WEST] == true) {
                    g.drawImage(imgLockedDoorW, Map.xdis, Map.ydis + (4 * GamePanel.TILESIZE), imgDoorW.getWidth() * 2, imgDoorW.getHeight() * 2, null);
                } else {
                    g.drawImage(imgUnlockedDoorW, Map.xdis, Map.ydis + (4 * GamePanel.TILESIZE), imgDoorW.getWidth() * 2, imgDoorW.getHeight() * 2, null);
                }
            } else if (lockedExits[Map.WEST] == true) {
                g.drawImage(imgLockedW, Map.xdis, Map.ydis + (4 * GamePanel.TILESIZE), imgDoorW.getWidth() * 2, imgDoorW.getHeight() * 2, null);
            } else {
                g.drawImage(imgDoorW, Map.xdis, Map.ydis + (4 * GamePanel.TILESIZE), imgDoorW.getWidth() * 2, imgDoorW.getHeight() * 2, null);
            }
        }

        //DRAW CHEST
        if (roomType == TREASURE || roomType == BOSSKEY) {
            //g.drawImage(imgChestClosed, Map.xdis + ((Map.COLS / 2) * GamePanel.TILESIZE), Map.ydis + ((Map.ROWS / 2) * GamePanel.TILESIZE), imgChestClosed.getWidth() * 2, imgChestClosed.getHeight() * 2, null);
        }

        if (chest != null) {
            chest.render(g);
        }

        //DRAW ENEMIES
        for (Enemy e : enemies) {
            e.render(g);
        }
        
        if(roomType == BOSS) {
            g.setColor(Color.WHITE);
            
            drawCenteredString(g, "THANK YOU FOR PLAYING THIS DEMO!", new Rectangle(0,0,GamePanel.VWIDTH,GamePanel.VHEIGHT), MenuState.menuFont);
            drawCenteredString(g, "PRESS 'OPTIONS' OR 'ESC' TO RETURN TO MENU", new Rectangle(0,32,GamePanel.VWIDTH,GamePanel.VHEIGHT), MenuState.menuFont);
        }

//        //DRAW STRING ROOM TYPE #
//        g.setColor(Color.CYAN);
//        g.drawString("" + roomType, 5, 50);
    }

    public void render(Graphics2D g, int x, int y) {
        for (int r = 0; r < intMap.length; r++) {
            for (int c = 0; c < intMap[0].length; c++) {
                switch (intMap[r][c]) {
                    case 0:
                        g.setColor(COLOR_FLOOR);
                        break;
                    case 1:
                        g.setColor(COLOR_WALL);
                        break;
                    case 2:
                        g.setColor(COLOR_DOOR);
                        break;
                }

                //g.fillRect(c * 32 +  150, r * 32 + 50, 32, 32);
                g.fillRect(Map.xdis + c * 32 + x, Map.ydis + r * 32 + y, 32, 32);
            }
        }

        g.drawImage(imgRoom, Map.xdis + x, Map.ydis + y, imgRoom.getWidth() * 2, imgRoom.getHeight() * 2, null);

        if (exits[Map.NORTH]) {
            if (map.getRoom(location.getRow() - 1, location.getCol()).getRoomType() == BOSS) {
                if (lockedExits[Map.NORTH] == true) {
                    g.drawImage(imgLockedDoorN, Map.xdis + x + (7 * GamePanel.TILESIZE), Map.ydis + y, imgDoorN.getWidth() * 2, imgDoorN.getHeight() * 2, null);
                } else {
                    g.drawImage(imgUnlockedDoorN, Map.xdis + x + (7 * GamePanel.TILESIZE), Map.ydis + y, imgDoorN.getWidth() * 2, imgDoorN.getHeight() * 2, null);
                }
            } else {
                g.drawImage(imgDoorN, Map.xdis + x + (7 * GamePanel.TILESIZE), Map.ydis + y, imgDoorN.getWidth() * 2, imgDoorN.getHeight() * 2, null);
            }
        }
        if (exits[Map.EAST]) {
            if (map.getRoom(location.getRow(), location.getCol() + 1).getRoomType() == BOSS) {
                if (lockedExits[Map.EAST] == true) {
                g.drawImage(imgLockedDoorE, Map.xdis + x + (Map.COLS - 2) * GamePanel.TILESIZE, Map.ydis + y + (4 * GamePanel.TILESIZE), imgDoorE.getWidth() * 2, imgDoorE.getHeight() * 2, null);
                } else {
                    g.drawImage(imgUnlockedDoorE, Map.xdis + x + (Map.COLS - 2) * GamePanel.TILESIZE, Map.ydis + y + (4 * GamePanel.TILESIZE), imgDoorE.getWidth() * 2, imgDoorE.getHeight() * 2, null);
                }
            } else {
                g.drawImage(imgDoorE, Map.xdis + x + (Map.COLS - 2) * GamePanel.TILESIZE, Map.ydis + y + (4 * GamePanel.TILESIZE), imgDoorE.getWidth() * 2, imgDoorE.getHeight() * 2, null);
            }
        }
        if (exits[Map.SOUTH]) {
            if (map.getRoom(location.getRow() + 1, location.getCol()).getRoomType() == BOSS) {
                if (lockedExits[Map.SOUTH] == true) {
                g.drawImage(imgLockedDoorS, Map.xdis + x + (7 * GamePanel.TILESIZE), Map.ydis + y + (Map.ROWS - 2) * GamePanel.TILESIZE, imgDoorS.getWidth() * 2, imgDoorS.getHeight() * 2, null);
                } else {
                    g.drawImage(imgUnlockedDoorS, Map.xdis + x + (7 * GamePanel.TILESIZE), Map.ydis + y + (Map.ROWS - 2) * GamePanel.TILESIZE, imgDoorS.getWidth() * 2, imgDoorS.getHeight() * 2, null);
                }
            } else {
                g.drawImage(imgDoorS, Map.xdis + x + (7 * GamePanel.TILESIZE), Map.ydis + y + (Map.ROWS - 2) * GamePanel.TILESIZE, imgDoorS.getWidth() * 2, imgDoorS.getHeight() * 2, null);
            }
        }
        if (exits[Map.WEST]) {
            if (map.getRoom(location.getRow(), location.getCol() - 1).getRoomType() == BOSS) {
                if (lockedExits[Map.SOUTH] == true) {
                g.drawImage(imgLockedDoorW, Map.xdis + x, Map.ydis + y + (4 * GamePanel.TILESIZE), imgDoorW.getWidth() * 2, imgDoorW.getHeight() * 2, null);
                } else {
                    g.drawImage(imgUnlockedDoorW, Map.xdis + x, Map.ydis + y + (4 * GamePanel.TILESIZE), imgDoorW.getWidth() * 2, imgDoorW.getHeight() * 2, null);
                }
            } else {
                g.drawImage(imgDoorW, Map.xdis + x, Map.ydis + y + (4 * GamePanel.TILESIZE), imgDoorW.getWidth() * 2, imgDoorW.getHeight() * 2, null);
            }
        }

        //DRAW CHEST
        if (roomType == TREASURE || roomType == BOSSKEY) {
            //g.drawImage(imgChestClosed, x + Map.xdis + ((Map.COLS / 2) * GamePanel.TILESIZE), y + Map.ydis + ((Map.ROWS / 2) * GamePanel.TILESIZE), imgChestClosed.getWidth() * 2, imgChestClosed.getHeight() * 2, null);
        }
        if (chest != null) {
            chest.render(g, x, y);
        }

        //DRAW ENEMIES
        for (Enemy e : enemies) {
            e.render(g, x, y);
        }
    }
    
    public void drawCenteredString(Graphics2D g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }

    
}
