/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

//STARTED ON APRIL 23, 2018
public class Main extends JFrame {

    public Main() {
        super("The Legend of Dungeon");
        add(new GamePanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(GamePanel.VWIDTH, GamePanel.VHEIGHT));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        try {
            //        setIconImage(Toolkit.getDefaultToolkit().getImage("src/Resources/Images/Menu/icon.png"));
            //setIconImage(ImageIO.read(new File("src/Resources/Images/Menu/icon.png")));
            InputStream stream = getClass().getResourceAsStream("/Resources/Images/Menu/icon.png");
            BufferedImage icon = ImageIO.read(stream);
            
//            setIconImage(getToolkit().getImage(getClass().getResource("/Resources/Images/icon.png")));
            setIconImage(icon);
            
            
            //open in fullscreen
            //setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH );
        } catch (Exception ex) {
            
        }
    }
    
    
    public static void main(String[] args) {

        Main game = new Main();
    }
    
}
