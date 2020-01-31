/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Main.GamePanel;
import Tilemap.Tile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 *
 * @author Reilly
 */
public class Map {

    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    private static final int MAXCHILDREN = 2;
    private static final int MINROOMS = 5;
    private static final int MAXROOMS = 10;

    public static final int ROWS = 11, COLS = 17;

    public static int xdis = (GamePanel.VWIDTH / 2) - (Map.COLS * GamePanel.TILESIZE / 2);
    public static int ydis = (GamePanel.VHEIGHT / 2) - (Map.ROWS * GamePanel.TILESIZE / 2);

    private Tile[][] tileset;

    private int[][][] roomTemplates;

    private Vector2 spawnRoom;
    private Vector2 currRoom;
    private Vector2 nextRoom = new Vector2(-1, -1);

    private boolean spawn;
    private boolean boss;
    private boolean bossKey;

    private int nextRoomX;
    private int nextRoomY;
    private int currRoomX;
    private int currRoomY;

    private int dx;
    private int dy;

    private int currNumRooms = 0;

    public boolean transition = false;
    private int transitionInt = -1;
    private static final int TRANSITION_SPEED = 16;

    //R x C PROCEDURALLY GENERATED DUNGEON
    //FIRST ROOM SPAWNS IN MIDDLE
    public Room[][] rooms;
    private Boolean[][] arrRoom;
    
    private boolean playerHasKey = false;

    public Map(int r, int c) {
        rooms = new Room[r][c];
        arrRoom = new Boolean[r][c];

        loadRoomData();

        //generateMap1();
        //generateMap2();
    }

    //LOADS THE ROOM TEMPLATES FROM ROOMDATA.DAT AND STORES THEM IN 3D ARRAY
    public void loadRoomData() {

        try {
            InputStream stream = getClass().getResourceAsStream("/Resources/Map/roomData2.dat");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            
            
            //READ IN DIMENSIONS
            String dimensions = br.readLine();
            Scanner s = new Scanner(dimensions);
            int x = s.nextInt();
            int y = s.nextInt();
            int z = s.nextInt();

            roomTemplates = new int[x][y][z];

            for (int i = 0; i < roomTemplates.length; i++) {
                //read empty line
                br.readLine();

                //read in each line and use delimiter
                String delims = "\\s+";
                for (int r = 0; r < roomTemplates[0].length; r++) {
                    String str = br.readLine();
                    String[] temp = str.split(delims);
                    for (int c = 0; c < roomTemplates[0][0].length; c++) {
                        roomTemplates[i][r][c] = Integer.parseInt(temp[c]);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void loadTileSet() {
//
//        try {
//            File file = new File("");
//            BufferedImage image = ImageIO.read(file);
//            int numTilesAcross = image.getWidth() / GamePanel.TILESIZE;
//
//            tileset = new Tile[2][numTilesAcross];
//
//            for (int r = 0; r < image.getHeight() / GamePanel.TILESIZE; r++) {
//                for (int c = 0; c < numTilesAcross; c++) {
//
//                    BufferedImage temp = image.getSubimage(c * GamePanel.TILESIZE, r * GamePanel.TILESIZE, GamePanel.TILESIZE, GamePanel.TILESIZE);
//                    tileset[r][c] = new Tile(temp, r);
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ERROR IN LOAD TILES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//            e.printStackTrace();
//        }
//    }

    public int[][] getRoomTemplate(int i) {
        return roomTemplates[i];
    }

    public Tile[][] getTileset() {
        return tileset;
    }

    public Vector2 getCurrRoom() {
        return currRoom;
    }

    public Vector2 getSpawnRoom() {
        return spawnRoom;
    }

    public void setCurrRoom(int r, int c) {
        currRoom.setX(r);
        currRoom.setY(c);
    }

    public Room getRoom(int r, int c) {
        return rooms[r][c];
    }
    
    public void setHasKey(boolean b) {
        playerHasKey = b;
    }
    
    public boolean playerHasKey() {
        return playerHasKey;
    }

//    public int getRoomType(int direction) {
//        switch (direction) {
//            case NORTH: 
//                break;
//            case EAST:
//                break;
//            case SOUTH:
//                break;
//            case WEST:
//                break;
//        }
//    }
    public void nextRoom(int direction) {

        if (rooms[currRoom.getRow()][currRoom.getCol()].hasExit(direction)) {

            transition = true;

            currRoomX = 0;
            currRoomY = 0;

//            prevRoom.setRow(currRoom.getRow());
//            prevRoom.setCol(currRoom.getCol());
            switch (direction) {
                case NORTH:
                    nextRoom.setRow(currRoom.getRow() - 1);
                    nextRoom.setCol(currRoom.getCol());
                    transitionInt = NORTH;

                    nextRoomX = 0;
                    nextRoomY = 0 - ROWS * GamePanel.TILESIZE - ydis;
                    break;
                case EAST:
                    nextRoom.setRow(currRoom.getRow());
                    nextRoom.setCol(currRoom.getCol() + 1);
                    transitionInt = EAST;

                    nextRoomX = COLS * GamePanel.TILESIZE + xdis;
                    nextRoomY = 0;
                    break;
                case SOUTH:
                    nextRoom.setRow(currRoom.getRow() + 1);
                    nextRoom.setCol(currRoom.getCol());
                    transitionInt = SOUTH;

                    nextRoomX = 0;
                    nextRoomY = ROWS * GamePanel.TILESIZE + ydis;
                    break;
                case WEST:
                    nextRoom.setRow(currRoom.getRow());
                    nextRoom.setCol(currRoom.getCol() - 1);
                    transitionInt = WEST;

                    nextRoomX = 0 - COLS * GamePanel.TILESIZE - xdis;
                    nextRoomY = 0;
                    break;
            }
        }
    }

    //0 = NORTH
    //1 = EAST
    //2 = SOUTH
    //3 = WEST
    private int currLocation;

    //WHEN GENERATING MAPS, IF ROOM IS A DEAD END, ADD TO LIST (?).
    //PICK A RANDOM ROOM FROM LIST THAT != SPAWN ROOM. = BOSS ROOM.
    //THE OTHER ROOMS WILL BECOME TREASURE ROOMS, WITH CHEST (COINS, ETC).
    //GENERATE MAP
    public void generateMap4() {
        
        playerHasKey = false;

        boss = false;
        spawn = false;
        bossKey = false;

        //generate spawn room
        //generate 0-2 child rooms
        //  -> Get random direction
        //  -> Check if empty / possible
        //  -> Generate child.
        //[repeat]
        //set arrRoom to false
        for (int rr = 0; rr < rooms.length; rr++) {
            for (int cc = 0; cc < rooms[0].length; cc++) {
                rooms[rr][cc] = null;
            }
        }

        //set currNumRooms to 0
        currNumRooms = 1;

        //PICK RANDOM NUMBER BETWEEN [0, SIZE - 1]
        int spawnLocation = (int) (Math.random() * (rooms.length * rooms[0].length));
        spawnRoom = new Vector2(spawnLocation / rooms.length, spawnLocation % rooms[0].length);
        currLocation = spawnLocation;

        rooms[spawnLocation / rooms.length][spawnLocation % rooms[0].length] = new Room(this, spawnLocation / rooms.length, spawnLocation % rooms[0].length);

        //PICK RANDOM NUMBER OF CHILDREN FOR SPAWN ROOM
        int numChildren = (int) (2 + (Math.random() * MAXCHILDREN + 1));

        for (int i = 0; i < numChildren; i++) {
            gen4(currLocation);
        }

        //ASSIGN CURRENT ROOM = SPAWN ROOM
        currRoom = new Vector2(spawnRoom.getRow(), spawnRoom.getCol());

        for (int r = 0; r < rooms.length; r++) {
            for (int c = 0; c < rooms[0].length; c++) {
                if (rooms[r][c] != null) {

                    if (r == spawnRoom.getRow() && c == spawnRoom.getCol()) {
                        rooms[r][c].setRoomType(Room.SPAWN);

                    } else if (rooms[r][c].getNumExits() == 1) {

                        if (rooms[r][c].getRoomType() != Room.SPAWN) {
                            //generate random number to see if boss room
                            int rnd = (int) (Math.round(Math.random()));

                            if (rnd == 1 && !boss) {
                                rooms[r][c].setRoomType(Room.BOSS);

                                boss = true;

                            } else if (!bossKey) {

                                rooms[r][c].setRoomType(Room.BOSSKEY);
                                bossKey = true;

                            } else {
                                rooms[r][c].setRoomType(Room.TREASURE);
                            }
                        }

                    } else {
                        rooms[r][c].setRoomType(Room.DEFAULT);
                        rooms[r][c].generateEnemies();
                    }

                }
            }
        }

        if (!(bossKey && boss)) {
            generateMap4();
        }

    }

    //CREATE BINARY TREE, RECURSE
    public void gen4(int loc) {

        boolean empty = false;

        //GET DIRECTION UNTIL NEIGHBOUR = FALSE
        int direction;

        boolean N = false;
        boolean E = false;
        boolean S = false;
        boolean W = false;

        do {
            direction = (int) (Math.random() * 4);

//                System.out.println("check dir:" + direction);
            //System.out.println(direction);
            switch (direction) {

                case NORTH:

                    empty = ((loc - rooms.length >= 0)
                            && (rooms[(loc - rooms.length) / rooms.length][(loc - rooms.length) % rooms.length] == null));
                    if (!empty) {
                        N = true;
                    }
                    break;
                case EAST:

                    empty = ((loc + 1) % 5 != 0) && (loc + 1 < (rooms.length * rooms.length))
                            && (rooms[(loc + 1) / rooms.length][(loc + 1) % rooms.length] == null);
                    if (!empty) {
                        E = true;
                    }
                    break;
                case SOUTH:
                    empty = ((loc + rooms.length < (rooms.length * rooms[0].length)))
                            && (rooms[(loc + rooms.length) / rooms.length][(loc + rooms.length) % rooms.length] == null);
                    if (!empty) {
                        S = true;
                    }
                    break;
                case WEST:
                    empty = (((loc - 1) % rooms.length != (rooms.length - 1)) && (loc - 1) >= 0)
                            && (rooms[(loc - 1) / rooms.length][(loc - 1) % rooms.length] == null);
                    if (!empty) {
                        W = true;
                    }
                    break;
            }

            if (N && E && S && W) {
                return;
            }

        } while (empty == false);

        //ADD EXIT IN DIRECTION TO CURRENT LOC 
        //  
        rooms[(loc) / rooms.length][(loc) % rooms.length].addExit(direction);

        switch (direction) {
            case NORTH:
                loc -= rooms.length;
                break;
            case EAST:
                loc += 1;
                break;
            case SOUTH:
                loc += rooms.length;
                break;
            case WEST:
                loc -= 1;
                break;
        }

        rooms[(loc) / rooms.length][(loc) % rooms.length] = new Room(this, (loc) / rooms.length, (loc) % rooms.length);
        currNumRooms++;

        //ADD EXIT TO NEW ROOM
        switch (direction) {
            case NORTH:
                rooms[(loc) / rooms.length][(loc) % rooms.length].addExit(SOUTH);
                break;
            case EAST:
                rooms[(loc) / rooms.length][(loc) % rooms.length].addExit(WEST);
                break;
            case SOUTH:
                rooms[(loc) / rooms.length][(loc) % rooms.length].addExit(NORTH);
                break;
            case WEST:
                rooms[(loc) / rooms.length][(loc) % rooms.length].addExit(EAST);
                break;
        }

        int numChildren = (int) (Math.random() * (MAXCHILDREN + 1));

        if (currNumRooms < MINROOMS) {
            numChildren = (int) (1 + (Math.random() * (MAXCHILDREN)));
        } else if (currNumRooms >= MAXROOMS) {
            return;
        } else {
            numChildren = (int) (Math.random() * (MAXCHILDREN + 1));
        }

        //System.out.println(numChildren);
        for (int i = 0; i < numChildren; i++) {
            gen4(loc);
        }
    }

    public void update() {

        if (transition) {
            switch (transitionInt) {
                case NORTH:
                    dx = 0;
                    dy = TRANSITION_SPEED;

                    if (nextRoomY >= 0 || nextRoomY + dy >= 0) {
                        transition = false;

                        currRoom.setRow(nextRoom.getRow());
                        currRoom.setCol(nextRoom.getCol());
                    }

                    break;
                case EAST:
                    dx = -TRANSITION_SPEED;
                    dy = 0;

                    if (nextRoomX <= 0 || nextRoomX + dx <= 0) {
                        transition = false;

                        currRoom.setRow(nextRoom.getRow());
                        currRoom.setCol(nextRoom.getCol());
                    }

                    break;
                case SOUTH:
                    dx = 0;
                    dy = -TRANSITION_SPEED;

                    if (nextRoomY <= 0 || nextRoomY + dy <= 0) {
                        transition = false;

                        currRoom.setRow(nextRoom.getRow());
                        currRoom.setCol(nextRoom.getCol());
                    }
                    break;
                case WEST:
                    dx = TRANSITION_SPEED;
                    dy = 0;

                    if (nextRoomX >= 0 || nextRoomX + dx >= 0) {
                        transition = false;

                        currRoom.setRow(nextRoom.getRow());
                        currRoom.setCol(nextRoom.getCol());
                    }
                    break;
            }

            nextRoomY += dy;
            nextRoomX += dx;

            currRoomY += dy;
            currRoomX += dx;

        } else if (rooms[currRoom.getRow()][currRoom.getCol()] != null) {
            rooms[currRoom.getRow()][currRoom.getCol()].update();
        }

    }
    
    public void update(double deltaTime) {
        if (transition) {
            switch (transitionInt) {
                case NORTH:
                    dx = 0;
                    dy = TRANSITION_SPEED;

                    if (nextRoomY >= 0 || nextRoomY + dy >= 0) {
                        transition = false;

                        currRoom.setRow(nextRoom.getRow());
                        currRoom.setCol(nextRoom.getCol());
                    }

                    break;
                case EAST:
                    dx = -TRANSITION_SPEED;
                    dy = 0;

                    if (nextRoomX <= 0 || nextRoomX + dx <= 0) {
                        transition = false;

                        currRoom.setRow(nextRoom.getRow());
                        currRoom.setCol(nextRoom.getCol());
                    }

                    break;
                case SOUTH:
                    dx = 0;
                    dy = -TRANSITION_SPEED;

                    if (nextRoomY <= 0 || nextRoomY + dy <= 0) {
                        transition = false;

                        currRoom.setRow(nextRoom.getRow());
                        currRoom.setCol(nextRoom.getCol());
                    }
                    break;
                case WEST:
                    dx = TRANSITION_SPEED;
                    dy = 0;

                    if (nextRoomX >= 0 || nextRoomX + dx >= 0) {
                        transition = false;

                        currRoom.setRow(nextRoom.getRow());
                        currRoom.setCol(nextRoom.getCol());
                    }
                    break;
            }

            nextRoomY += dy;
            nextRoomX += dx;

            currRoomY += dy;
            currRoomX += dx;

        } else if (rooms[currRoom.getRow()][currRoom.getCol()] != null) {
            rooms[currRoom.getRow()][currRoom.getCol()].update(deltaTime);
        }
    }

    public void render(Graphics2D g) {

        if (!transition) {
            //RENDER ROOM
            if (spawnRoom != null) {
                if (rooms[currRoom.getRow()][currRoom.getCol()] != null) {
                    rooms[currRoom.getRow()][currRoom.getCol()].render(g);
                }
            }
        } else if (spawnRoom != null) {
            if (rooms[currRoom.getRow()][currRoom.getCol()] != null) {
                rooms[currRoom.getRow()][currRoom.getCol()].render(g, currRoomX, currRoomY);
            }

            if (rooms[nextRoom.getRow()][nextRoom.getCol()] != null) {
                rooms[nextRoom.getRow()][nextRoom.getCol()].render(g, nextRoomX, nextRoomY);
            }
        }

        //RENDER MAP
        int xdiff = GamePanel.VWIDTH - rooms[0].length * 16;
        //int ydiff = GamePanel.VWIDTH - rooms[0].length * 16;

        g.setColor(Color.BLACK);

        Color cool = new Color(0, 0, 0, 100);

        g.setColor(cool);

        g.fillRect(xdiff - 8, 8, rooms[0].length * 16, rooms.length * 16);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2f));

        g.drawRect(xdiff - 8, 8, rooms[0].length * 16, rooms.length * 16);

        g.setStroke(new BasicStroke(1f));

        for (int r = 0; r < rooms.length; r++) {
            for (int c = 0; c < rooms[0].length; c++) {

                if (rooms[r][c] != null) {

                    //determine room color
                    if (r == currRoom.getRow() && c == currRoom.getCol()) {
                        g.setColor(Color.LIGHT_GRAY);
                    } else if (rooms[r][c].getRoomType() == Room.BOSS) {
                        g.setColor(Color.RED);
                    } else if (rooms[r][c].getRoomType() == Room.BOSSKEY) {
                        g.setColor(Color.MAGENTA);
                    } else if (rooms[r][c].getRoomType() == Room.TREASURE) {
                        g.setColor(Color.YELLOW);
                    } else {
                        g.setColor(Color.DARK_GRAY);
                    }

                    //draw room
                    g.fillRect(xdiff - 4 + c * 10 + c * 5, 12 + r * 10 + r * 5, 10, 10);
                    g.setColor(Color.BLACK);
                    g.drawRect(xdiff - 4 + c * 10 + c * 5, 12 + r * 10 + r * 5, 10, 10);
                    g.setColor(Color.DARK_GRAY);

                    //draw doors
                    if (rooms[r][c].hasExit(NORTH)) {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(xdiff - 4 + c * 10 + c * 5 + 2, 12 + r * 10 + r * 5 - 5, 6, 5);
                        g.setColor(Color.BLACK);
                        g.drawRect(xdiff - 4 + c * 10 + c * 5 + 2, 12 + r * 10 + r * 5 - 5, 6, 5);
                    }
                    if (rooms[r][c].hasExit(EAST)) {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(xdiff - 4 + c * 10 + c * 5 + 10, 12 + r * 10 + r * 5 + 2, 5, 6);
                        g.setColor(Color.BLACK);
                        g.drawRect(xdiff - 4 + c * 10 + c * 5 + 10, 12 + r * 10 + r * 5 + 2, 5, 6);
                    }
                    if (rooms[r][c].hasExit(SOUTH)) {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(xdiff - 4 + c * 10 + c * 5 + 2, 12 + r * 10 + r * 5 + 10, 6, 5);
                        g.setColor(Color.BLACK);
                        g.drawRect(xdiff - 4 + c * 10 + c * 5 + 2, 12 + r * 10 + r * 5 + 10, 6, 5);
                    }
                    if (rooms[r][c].hasExit(WEST)) {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(xdiff - 4 + c * 10 + c * 5 - 5, 12 + r * 10 + r * 5 + 2, 5, 6);
                        g.setColor(Color.BLACK);
                        g.drawRect(xdiff - 4 + c * 10 + c * 5 - 5, 12 + r * 10 + r * 5 + 2, 5, 6);
                    }
                }
            }
        }

    }    

}
