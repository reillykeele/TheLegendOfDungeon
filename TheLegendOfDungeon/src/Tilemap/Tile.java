/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tilemap;

import java.awt.image.BufferedImage;

/**
 *
 * @author 28783
 */
public class Tile {
    
    public static final int PASSABLE = 0;
    public static final int BLOCKED = 1;

    private BufferedImage image;
    private int type;

    public Tile(BufferedImage image, int type) {
        this.image = image;
        this.type = type;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getType() {
        return type;
    }

}
