/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateMachine;

import Main.Audio;
import Main.AudioPlayer;
//import Main.ControllerHandler;
import Main.GamePanel;
import static Main.GamePanel.font;
import static Main.GamePanel.fpsFont;
import Main.InputManager;
import Main.KeyHandler;
import Main.Settings;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import static java.awt.event.KeyEvent.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class MenuState extends State {

    private ArrayList<String> menu;
    private ArrayList<String> settingsMenu;
    private int currSelected = 0;

    private BufferedImage title;

    private boolean start;

    public static Font menuFont;
    public static Font menuFont16;
    public static Font menuFont32;

    private long lastBlink;
    private long timeBetweenBlinks = (long) (1000000000 * .75);
    private boolean blinking;

    private boolean settings;
    private boolean controls;

    public MenuState(StateMachine sm) {
        super(sm);
        init();
    }

    public void init() {
        menu = new ArrayList<String>();
        settingsMenu = new ArrayList<String>();

        menu.add("START NEW GAME");
        menu.add("SETTINGS");
        menu.add("HOW TO PLAY");
        menu.add("QUIT");

        settingsMenu.add("MUTED: FALSE");
        settingsMenu.add("SETTING");
        settingsMenu.add("SETTING");
        settingsMenu.add("RETURN");

        start = false;

        try {
            InputStream stream = getClass().getResourceAsStream("/Resources/Images/Menu/title.gif");
            //InputStream fontStream = getClass().getResourceAsStream("/Resources/Fonts/fffont.ttf");
            
            Font f = GamePanel.font;
            
            title = ImageIO.read(stream);

//            menuFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            menuFont = font.deriveFont(24f);

//            menuFont16 = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            menuFont16 = font.deriveFont(16f);

//            menuFont32 = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            menuFont32 = font.deriveFont(64f);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void select(String s, int n) {

        if (s.equals("MENU")) {
            switch (n) {
                case 0:
                    Audio.MENUMUSIC.stop();
                    sm.loadState(StateMachine.PLAYSTATE);
                    break;
                case 1:
                    settings = true;
                    currSelected = 0;
                    break;
                case 2:
                    controls = true;
                    currSelected = 0;
                    break;
                case 3:
                    System.exit(0);
                    break;
            }
        } else if (s.equals("SETTINGS")) {

            switch (n) {
                case 0:
                    Settings.MUTED.setTrue(!Settings.MUTED.isTrue());

                    if (Settings.MUTED.isTrue()) {
                        settingsMenu.set(0, "MUTED : TRUE");
                        Audio.MENUMUSIC.stop();
                    } else {
                        settingsMenu.set(0, "MUTED : FALSE");
                        Audio.MENUMUSIC.play(true);
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    settings = false;
                    currSelected = 0;
                    break;
                case 4:
                    break;
                case 5:
                    break;

            }
        } else if (s.equals("CONTROLS")) {

        }

    }

    @Override
    public void resetState() {
        currSelected = 0;
        Audio.MENUMUSIC.play(true);

        if (Settings.MUTED.isTrue()) {
            settingsMenu.set(0, "MUTED : TRUE");
        } else {
            settingsMenu.set(0, "MUTED : FALSE");
        }

        start = false;
    }

    @Override
    public void update() {
        if (lastBlink + timeBetweenBlinks <= System.nanoTime()) {
            lastBlink = System.nanoTime();
            blinking = !blinking;
        }
    }
    
    @Override
    public void update(double deltaTime) {
        if (lastBlink + timeBetweenBlinks <= System.nanoTime()) {
            lastBlink = System.nanoTime();
            blinking = !blinking;
        }
    }

    @Override
    public void render(Graphics2D g) {

        g.drawImage(title, 0, 0, null);

        g.setColor(Color.WHITE);
        drawCenteredString(g, "REILLY KEELE (2018)", new Rectangle(0, 165, GamePanel.VWIDTH, GamePanel.VHEIGHT),
                menuFont16);

        if (start == true) {

            if (!settings && !controls) {
                for (int i = 0; i < menu.size(); i++) {
                    if (i == currSelected) {
                        g.setColor(new Color(238, 28, 36, 255));
                    } else {
                        g.setColor(Color.WHITE);
                    }

                    drawCenteredString(g, menu.get(i), new Rectangle(0, 50 + (i * 25), GamePanel.VWIDTH, GamePanel.VHEIGHT),
                            menuFont);

                    //g.drawString(menu.get(i), 50, 50 + (i * 15));
                }
            } else if (settings) {
                for (int i = 0; i < settingsMenu.size(); i++) {
                    if (i == currSelected) {
                        g.setColor(new Color(238, 28, 36, 255));
                    } else {
                        g.setColor(Color.WHITE);
                    }

                    drawCenteredString(g, settingsMenu.get(i), new Rectangle(0, 50 + (i * 25), GamePanel.VWIDTH, GamePanel.VHEIGHT),
                            menuFont);

                    //g.drawString(menu.get(i), 50, 50 + (i * 15));
                }
            } else if (controls) {

                String[] arr = {
                    "MOVE : ARROW KEYS",
                    "ATTACK / INTERACT : A",
                    "PAUSE / BACK : ESC",
                    "----------------------",
                    "COLLECT THE KEY AND UNLOCK THE FINAL DOOR TO WIN",
                    "PRESS ESC TO RETURN"
                };

                for (int i = 0; i < arr.length; i++) {
                    drawCenteredString(g, arr[i], new Rectangle(0, 15 + (i * 25), GamePanel.VWIDTH, GamePanel.VHEIGHT),
                            menuFont);
                }
            }
        } else {
            g.setColor(Color.white);
            if (blinking) {
                drawCenteredString(g, "PUSH ENTER", new Rectangle(0, 70, GamePanel.VWIDTH, GamePanel.VHEIGHT),
                        menuFont);
            }
            //g.drawString("PUSH ANY BUTTON", GamePanel.VWIDTH / 2, GamePanel.VHEIGHT / 2);
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

    @Override
    public void keyPressed(int k) {
//        if (k == VK_ENTER) {
//            select(currSelected);
//        }
//        if (k == VK_DOWN) {
//            if (currSelected < menu.size() - 1) {
//                currSelected++;
//            } else {
//                currSelected = 0;
//            }
//        }
//        if (k == VK_UP) {
//            if (currSelected > 0) {
//                currSelected--;
//            } else {
//                currSelected = menu.size() - 1;
//            }
//        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void processInput() {
//        if (input.isKeyDown(VK_ENTER)) {
//            select(currSelected);
//        }
//        if (input.isKeyDown(VK_DOWN)) {
//            if (currSelected < menu.size() - 1) {
//                currSelected++;
//            } else {
//                currSelected = 0;
//            }
//        }
//        if (input.isKeyDown(VK_UP)) {
//            if (currSelected > 0) {
//                currSelected--;
//            } else {
//                currSelected = menu.size() - 1;
//            }
//        }

        if (!start) {

            if (KeyHandler.anyKeyPress() ) {
                start = true;
                Audio.MENUSELECT.play(false);
            }

        } else {

            if (!settings && !controls) {
                //MAIN MENU
                if (KeyHandler.isPressed(KeyHandler.ENTER) ) {
                    select("MENU", currSelected);
                    //sounds[SELECT].play();
                    Audio.MENUSELECT.play(false);
                }
                if (KeyHandler.isPressed(KeyHandler.DOWN)) {
                    if (currSelected < menu.size() - 1) {
                        currSelected++;
                    } else {
                        currSelected = 0;
                    }

                    Audio.MENUMOVE.play(false);
                }
                if (KeyHandler.isPressed(KeyHandler.UP) ) {
                    if (currSelected > 0) {
                        currSelected--;
                    } else {
                        currSelected = menu.size() - 1;
                    }
                    Audio.MENUMOVE.play(false);
                }
            } else if (settings) {
                //SETTINGS
                if (KeyHandler.isPressed(KeyHandler.ENTER) ) {
                    select("SETTINGS", currSelected);
                    //sounds[SELECT].play();
                    Audio.MENUSELECT.play(false);
                }
                if (KeyHandler.isPressed(KeyHandler.ESCAPE) ) {
                    settings = false;
                    currSelected = 0;
                    Audio.MENUCANCEL.play(false);
                }
                if (KeyHandler.isPressed(KeyHandler.DOWN) ) {
                    if (currSelected < settingsMenu.size() - 1) {
                        currSelected++;
                    } else {
                        currSelected = 0;
                    }

                    Audio.MENUMOVE.play(false);
                }
                if (KeyHandler.isPressed(KeyHandler.UP) ) {
                    if (currSelected > 0) {
                        currSelected--;
                    } else {
                        currSelected = settingsMenu.size() - 1;
                    }
                    Audio.MENUMOVE.play(false);
                }
            } else if (controls) {
                if (KeyHandler.isPressed(KeyHandler.ESCAPE) ) {
                    controls = false;
                    currSelected = 0;
                    Audio.MENUCANCEL.play(false);
                }
            }
        }
    }
}
