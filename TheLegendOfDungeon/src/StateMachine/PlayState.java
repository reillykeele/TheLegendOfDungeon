/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateMachine;

import Entity.Player;
import Main.Audio;
import Main.AudioPlayer;
//import Main.ControllerHandler;
import Main.GamePanel;
import Main.InputManager;
import Main.KeyHandler;
import Main.Settings;
import static StateMachine.MenuState.menuFont;
import World.Map;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import static java.awt.event.KeyEvent.*;
import java.util.ArrayList;

public class PlayState extends State {

    public static final int RESUME = 0;
    public static final int RESTART = 1;
    public static final int SETTINGS = 2;
    public static final int MENU = 3;
    public static final int QUIT = 4;

    private Map map;
    private Player player;

    private ArrayList<String> pauseMenu;
    private ArrayList<String> deathMenu;
    private ArrayList<String> settingsMenu;
    private int currSelected;

    public boolean paused = false;

    private boolean settings;

    //SOUND CONSTANTS
    public PlayState(StateMachine sm) {
        super(sm);
        init();
    }

    public void init() {
        map = new Map(5, 5);
        map.generateMap4();
        player = new Player(map, 8 * 32, 5 * 32);

        pauseMenu = new ArrayList<String>();
        deathMenu = new ArrayList<String>();
        settingsMenu = new ArrayList<String>();

        pauseMenu.add("RESUME GAME");
        pauseMenu.add("RESTART");
        pauseMenu.add("SETTINGS");
        pauseMenu.add("RETURN TO MENU");
        pauseMenu.add("QUIT");

        deathMenu.add("START NEW GAME");
        deathMenu.add("RETURN TO MENU");
        deathMenu.add("QUIT");

        if (Settings.MUTED.isTrue()) {
            settingsMenu.add("MUTED: TRUE");
        } else {
            settingsMenu.add("MUTED: FALSE");
        }
        settingsMenu.add("SETTING");
        settingsMenu.add("SETTING");
        settingsMenu.add("RETURN");

        //sounds[YEET] = new AudioPlayer("src/Resources/Audio/Sounds/YEET.wav", false);
    }

    @Override
    public void resetState() {
        Audio.GAMEMUSIC.play(true);

        currSelected = 0;
        paused = false;
        
        if(Settings.MUTED.isTrue()) {
            settingsMenu.set(0, "MUTED : TRUE");
        } else {
            settingsMenu.set(0, "MUTED : FALSE");
        }

        map = null;
        player = null;
        pauseMenu = null;

        init();
    }

    public void select(String s, int n) {
        if (s.equals("PAUSE")) {
            if (!settings) {
                switch (n) {
                    case RESUME:
                        paused = false;
                        break;

                    case RESTART:
                        Audio.GAMEMUSIC.stop();
                        Audio.GAMEMUSIC.play(true);

                        player = null;
                        map.generateMap4();
                        player = new Player(map, 8 * 32, 5 * 32);

                        paused = false;
                        break;

                    case SETTINGS:
                        currSelected = 0;
                        settings = true;
                        break;

                    case MENU:

                        Audio.GAMEMUSIC.stop();
                        sm.loadState(StateMachine.MENUSTATE);

                        break;

                    case QUIT:
                        System.exit(0);
                        break;
                }

            } else {

                switch (n) {

                    case 0:
                        Settings.MUTED.setTrue(!Settings.MUTED.isTrue());

                        if (Settings.MUTED.isTrue()) {
                            settingsMenu.set(0, "MUTED : TRUE");
                            Audio.GAMEMUSIC.stop();
                        } else {
                            settingsMenu.set(0, "MUTED : FALSE");
                            Audio.GAMEMUSIC.play(true);
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

            }
        } else {
            switch (n) {
                case 0:
                    Audio.GAMEMUSIC.stop();
                    Audio.GAMEMUSIC.play(true);

                    player = null;
                    map.generateMap4();
                    player = new Player(map, 8 * 32, 5 * 32);

                    paused = false;
                    break;

                case 1:

                    Audio.GAMEMUSIC.stop();
                    sm.loadState(StateMachine.MENUSTATE);

                    break;

                case 2:
                    System.exit(0);
                    break;
            }
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
    public void update() {
        if (!paused) {
            if (!player.getDead()) {
                if (Audio.GAMEMUSIC.paused()) {
                    Audio.GAMEMUSIC.resume();
                }

                map.update();

                if (player != null) {
                    player.update();
                }
            } else {
                //if player dead
                Audio.GAMEMUSIC.stop();
            }
        } else {
            Audio.GAMEMUSIC.pause();
        }
    }

    @Override
    public void render(Graphics2D g) {
        map.render(g);
        player.render(g);

        if (paused) {
//            g.setFont(GamePanel.fpsFont);
//            g.setColor(Color.GREEN);
//            g.drawString("PAUSED", GamePanel.VWIDTH / 2 - 50, GamePanel.VHEIGHT / 2);

            g.setColor(Color.BLACK);
            g.fillRect(GamePanel.VWIDTH / 2 - 90, GamePanel.VHEIGHT / 2 - (pauseMenu.size() * 25 + 50) / 2, 180, pauseMenu.size() * 25 + 50);
            g.setFont(MenuState.menuFont);

            if (!settings) {

                for (int i = 0; i < pauseMenu.size(); i++) {
                    if (i == currSelected) {
                        g.setColor(new Color(238, 28, 36, 255));
                    } else {
                        g.setColor(Color.WHITE);
                    }

                    drawCenteredString(g, pauseMenu.get(i), new Rectangle(0, -50 + (i * 25), GamePanel.VWIDTH, GamePanel.VHEIGHT),
                            MenuState.menuFont);

                    //g.drawString(menu.get(i), 50, 50 + (i * 15));
                }
            } else {
                for (int i = 0; i < settingsMenu.size(); i++) {
                    if (i == currSelected) {
                        g.setColor(new Color(238, 28, 36, 255));
                    } else {
                        g.setColor(Color.WHITE);
                    }

                    drawCenteredString(g, settingsMenu.get(i), new Rectangle(0, -50 + (i * 25), GamePanel.VWIDTH, GamePanel.VHEIGHT),
                            MenuState.menuFont);

                    //g.drawString(menu.get(i), 50, 50 + (i * 15));
                }
            }
        }

        if (player.getDead()) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GamePanel.VWIDTH, GamePanel.VHEIGHT);

            g.setColor(Color.red);
            drawCenteredString(g, "YOU DIED", new Rectangle(0, 0, GamePanel.VWIDTH, 150),
                    MenuState.menuFont32);

            for (int i = 0; i < deathMenu.size(); i++) {
                if (i == currSelected) {
                    g.setColor(new Color(238, 28, 36, 255));
                } else {
                    g.setColor(Color.WHITE);
                }

                drawCenteredString(g, deathMenu.get(i), new Rectangle(0, -25 + (i * 25), GamePanel.VWIDTH, GamePanel.VHEIGHT),
                        MenuState.menuFont);

                //g.drawString(menu.get(i), 50, 50 + (i * 15));
            }
        }
    }

    @Override
    public void keyPressed(int k) {
//        if (k == VK_ENTER) {
//            player = null;
//
//            map.generateMap4();
//
//            player = new Player(map, 8 * 32, 5 * 32);
//        }
//
//        if (k == VK_ESCAPE) {
//            paused = !paused;
//        }
//        
//        if(k == VK_O) {
//            map.getRoom(map.getCurrRoom().getRow(),map.getCurrRoom().getCol()).unlockDoors();
//        }

//        if (k == VK_UP) {
//            map.nextRoom(Map.NORTH);
//        }
//        if (k == VK_RIGHT) {
//            map.nextRoom(Map.EAST);
//        }
//        if (k == VK_DOWN) {
//            map.nextRoom(Map.SOUTH);
//        }
//        if (k == VK_LEFT) {
//            map.nextRoom(Map.WEST);
//        }
//        player.KeyPressed(k);
    }

    @Override
    public void keyReleased(int k) {
//        player.keyReleased(k);
    }

    @Override
    public void processInput() {
//        if (input.isKeyDown(VK_ENTER)) {
//            player = null;
//
//            map.generateMap4();
//
//            player = new Player(map, 8 * 32, 5 * 32);
//        }
//
//        if (input.isKeyDown(VK_ESCAPE)) {
//            paused = !paused;
//        }
//        
//        if(input.isKeyDown(VK_O)) {
//            map.getRoom(map.getCurrRoom().getRow(),map.getCurrRoom().getCol()).unlockDoors();
//        }

        if (paused) {
            //pause menu
            if (!settings) {
                if (KeyHandler.isPressed(KeyHandler.ENTER) ) {
                    select("PAUSE", currSelected);
                    //sounds[SELECT].play();
                    Audio.MENUSELECT.play(false);
                }
//                if (ControllerHandler.isPressed(ControllerHandler.CIRCLE)) {
//                    paused = !paused;
//                    currSelected = 0;
//                }
                if (KeyHandler.isPressed(KeyHandler.DOWN) ) {
                    if (currSelected < pauseMenu.size() - 1) {
                        currSelected++;
                    } else {
                        currSelected = 0;
                    }

                    Audio.MENUMOVE.play(false);
                }
                if (KeyHandler.isPressed(KeyHandler.UP)) {
                    if (currSelected > 0) {
                        currSelected--;
                    } else {
                        currSelected = pauseMenu.size() - 1;
                    }
                    Audio.MENUMOVE.play(false);
                }
            } else {
                if (KeyHandler.isPressed(KeyHandler.ENTER) ) {
                    select("PAUSE", currSelected);
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
            }

        } else {
            if (!player.getDead()) {

                player.processInput();

                if (KeyHandler.isPressed(KeyHandler.ENTER)) {
                    player = null;

                    map.generateMap4();

                    player = new Player(map, 8 * 32, 5 * 32);
                }

                if (KeyHandler.isPressed(KeyHandler.BUTTON2)) {
                    map.getRoom(map.getCurrRoom().getRow(), map.getCurrRoom().getCol()).unlockDoors();
                }
            } else {
                //IF PLAYER IS DEAD
                if (KeyHandler.isPressed(KeyHandler.ENTER)) {
                    select("DEATH", currSelected);
                    //sounds[SELECT].play();
                    Audio.MENUSELECT.play(false);
                }
                if (KeyHandler.isPressed(KeyHandler.DOWN)) {
                    if (currSelected < deathMenu.size() - 1) {
                        currSelected++;
                    } else {
                        currSelected = 0;
                    }

                    Audio.MENUMOVE.play(false);
                }
                if (KeyHandler.isPressed(KeyHandler.UP)) {
                    if (currSelected > 0) {
                        currSelected--;
                    } else {
                        currSelected = deathMenu.size() - 1;
                    }
                    Audio.MENUMOVE.play(false);
                }
            }
        }

        if (KeyHandler.isPressed(KeyHandler.ESCAPE)) {
            if (!player.getDead()) {

                paused = !paused;
                currSelected = 0;
            }
        }

//        if (KeyHandler.isPressed(KeyHandler.ENTER)) {
//            map.getRoom(map.getCurrRoom().getRow(), map.getCurrRoom().getCol()).unlockDoors();
//        }        
    }

}
