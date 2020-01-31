/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tilemap;

/**
 *
 * @author 28783
 */
import Main.GamePanel;
import StateMachine.State;
import World.Map;
import World.Room;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;

public class TileMap {

    private Room room;

    private double x, y;

    private int[][] map;
    private int tileSize = GamePanel.TILESIZE;
    private int numRows, numCols;
    private int width, height;

    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles;

    private int rowOffset, colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    public TileMap(Room room) {
        this.room = room;
        
        this.tileSize = Main.GamePanel.TILESIZE;
    }

    public void loadTiles(String s) {

        try {
            File file = new File(s);
            tileset = ImageIO.read(file);
            numTilesAcross = tileset.getWidth() / tileSize;
            
            tiles = new Tile[2][numTilesAcross];

            for (int r = 0; r < tileset.getHeight() / tileSize; r++) {
                for (int c = 0; c < numTilesAcross; c++) {

                    BufferedImage temp = tileset.getSubimage(c * tileSize, r * tileSize, tileSize, tileSize);
                    tiles[r][c] = new Tile(temp, r);
                    System.out.print(tiles[r][c].getType() + " ");
                }
                System.out.println("");
            }

        } catch (Exception e) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ERROR IN LOAD TILES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public double getx() {
        return x;
    }

    public double gety() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getType(int row, int col) {

        int rc = map[row][col];
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;

        return tiles[r][c].getType();
    }
    
    public void update() {

    }

    public void render(Graphics2D g) {

//        g.drawImage(tileset, 0, 0, null);
//
//        g.setColor(Color.DARK_GRAY);
//        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {

                int rx = map[r][c] / numTilesAcross;
                int cx = map[r][c] % numTilesAcross;

                //g.drawImage(tiles[rx][cx].getImage(), (int) (c * ((int) (GamePanel.SCALE * GamePanel.TILESIZE)) - state.getCamera().getXOffset()), (int) (r * ((int) (GamePanel.SCALE * GamePanel.TILESIZE)) - state.getCamera().getYOffset()), (int) (GamePanel.SCALE * GamePanel.TILESIZE), (int) (GamePanel.SCALE * GamePanel.TILESIZE), null);

            }
        }

    }
}