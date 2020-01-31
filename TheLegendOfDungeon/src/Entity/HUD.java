/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author 28783
 */
public class HUD {

    private Player player;

    public HUD(Player player) {
        this.player = player;
    }

    public void update() {

    }

    public void render(Graphics2D g) {

        for (int i = 0; i < player.getHealth(); i++) {
            g.setColor(Color.RED);
            g.fillRect(8 + i * 10 + i * 2, 8, 8, 8);
        }

        g.setColor(Color.YELLOW);
        g.fillOval(8, 22, 8, 8);
        g.drawString("x " + player.getCoins(), 20, 30);
        
        if(player.getBossKey()) {
            g.fillRect(8, 36, 8, 8);
        }

    }

}
