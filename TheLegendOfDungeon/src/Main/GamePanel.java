/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import StateMachine.StateMachine;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JPanel;

/**
 *
 * @author 28783
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {

    //ASPECT RATIO: 16:9
    //1280:720 = 16:9 * 80
    //VIRTUAL WIDTH / HEIGHT. WILL BE SCALED UP 
    //1920 x 1080
    public static final double SCALE = 2.00;
    public static final int VWIDTH = (int) (1280 / SCALE), VHEIGHT = (int) (720 / SCALE);

    public static final int TILESIZE = 32;

    //RENDER
    public static final int WIDTH = 1280, HEIGHT = 720;
    public static int currWidth = WIDTH, currHeight = HEIGHT;
    private static int screenX = 0, screenY = 0;

    public static Font font;
    public static Font fpsFont;

    private Thread thread;
    private BufferedImage renderer;
    private Graphics2D g2d;

    private StateMachine stateMachine;

    private static final int TARGET_RENDER_FPS = 120;
    private static final int TARGET_UPDATE_FPS = 60;
    private static final long OPTIMAL_TIME = 1000000000 / TARGET_RENDER_FPS;
    private long initialStartTime;

    private int time = 0;

    private float interpolation;

    private boolean running;
    private boolean paused = false;
    

    public GamePanel() {
        super();
        setPreferredSize(new Dimension(VWIDTH, VHEIGHT));
        setFocusable(true);
        requestFocus();
        
    }

    //INITIALIZE THREAD
    @Override
    public void addNotify() {
        super.addNotify();

//        ControllerHandler.searchForControllers();
        
        addKeyListener(this);
        //addKeyListener(input);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        //set up render
        renderer = new BufferedImage(VWIDTH, VHEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2d = renderer.createGraphics();

        try {
            
            InputStream stream = getClass().getResourceAsStream("/Resources/Fonts/fffont.ttf");

            font = Font.createFont(Font.TRUETYPE_FONT, stream);
            font = font.deriveFont(20f);
            //fpsFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Resources/Fonts/forwa.TTF"));
            //fpsFont = fpsFont.deriveFont(20f);

        } catch (Exception e) {
            e.printStackTrace();
        }

        stateMachine = new StateMachine();

        running = true;

        gameLoop6();
    }

    public void gameLoop() {

        //TRACK TOTAL GAME TIME
        initialStartTime = System.nanoTime();

        long lastTime = System.nanoTime();
        long lastFPSTime = 0;

        while (running) {

            long currentTime = System.nanoTime();
            long updateLength = currentTime - lastTime;
            lastTime = currentTime;
            double dt = updateLength / ((double) OPTIMAL_TIME);

            System.out.println(dt);

            lastFPSTime += updateLength;

            if (lastFPSTime >= 1000000000) {
                lastFPSTime = 0;
            }

            //System.out.println(dt);
            //update(dt);
            repaint();

            try {
                long gameTime = (lastTime - currentTime + OPTIMAL_TIME) / 1000000;
                //System.out.println(gameTime);
                Thread.sleep(gameTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            repaint();

        }
    }

    private int fps = 0;
    private int frameCount = 0;

    private void gameLoop2() {
        //This value would probably be stored elsewhere.
        final double GAME_HERTZ = 30.0;

        //Calculate how many ns each frame should take for our target game hertz.
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;

        //At the very most we will update the game this many times before a new render.
        //If you're worried about visual hitches more than perfect timing, set this to 1.
        final int MAX_UPDATES_BEFORE_RENDER = 1;

        //We will need the last update time.
        double lastUpdateTime = System.nanoTime();

        //Store the last time we rendered.
        double lastRenderTime = System.nanoTime();

        //If we are able to get as high as this FPS, don't render again.
        final double TARGET_RENDER_FPS = 1;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_RENDER_FPS;

        //Simple way of finding FPS.
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (running) {
            double now = System.nanoTime();
            int updateCount = 0;

            if (!paused) {
                //Do as many game updates as we need to, potentially playing catchup.
                while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                    update();
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }

                //If for some reason an update takes forever, we don't want to do an insane number of catchups.
                //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }

                //Render. To do so, we need to calculate interpolation for a smooth render.
                float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
                drawGame(interpolation);
                lastRenderTime = now;

                //Update the frames we got.
                int thisSecond = (int) (lastUpdateTime / 1000000000);
                if (thisSecond > lastSecondTime) {
                    System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
                    time++;
                    fps = frameCount;
                    frameCount = 0;
                    lastSecondTime = thisSecond;
                }

                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield();

                    //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
                    //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
                    //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                    }

                    now = System.nanoTime();
                }
            }
        }
    }

    private long gameTime;

    public void gameLoop3() {
        long lastLoopTime = System.nanoTime();

        final int GAME_HERTZ = 30;

        final long OPTIMAL_TIME = 1000000000 / GAME_HERTZ;
        long lastFpsTime = 0;
        while (true) {            
            
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double) OPTIMAL_TIME);

            lastFpsTime += updateLength;
            if (lastFpsTime >= 1000000000) {
                lastFpsTime = 0;
            }

            update();

            repaint();

            try {
                gameTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
                System.out.println(gameTime);
                Thread.sleep(gameTime);
            } catch (Exception e) {
            }
        }
    }
    
    public void gameLoop4() {
        while(true){
            update();
            repaint();
            
            try {
                thread.sleep(33);
            } catch(Exception e) {
                
            }
        }
    }
    
    public void gameLoop6() {      
        
        initialStartTime = System.nanoTime();
        
        final long framesPerNano = (long) (TARGET_UPDATE_FPS * Math.pow(10, 9));
        final long nanoPerFrame = (long) (1000000000 / TARGET_UPDATE_FPS);
        
        final long renderFPN = (long) (TARGET_RENDER_FPS * Math.pow(10, 9));
        final long renderNPF = (long) (1000000000 / TARGET_RENDER_FPS);
               
        long sinceLastUpdate = 0;
        long sinceLastRender = 0;
        
        long previous = System.nanoTime();
        double lag = 0.0;
        
        int renderFrameCount = 0;
        long sinceLastSecond = 0; 
        
        while(running) {
            long current = System.nanoTime();
            long elapsed = current - previous;
            previous = current;
            
            lag += elapsed;                        
            
            sinceLastUpdate += elapsed;
            if(sinceLastUpdate >= nanoPerFrame) {
                update(nanoToSec(sinceLastUpdate));
                sinceLastUpdate = 0;
            }
            
            sinceLastRender += System.nanoTime() - previous;
            if(sinceLastRender >= renderNPF) {
                repaint();
                renderFrameCount++;
                sinceLastRender = 0;
            }                      
             
            sinceLastSecond += elapsed;
            if (sinceLastSecond > 1000000000) {                    
                    fps = renderFrameCount;
                    time++;
                    renderFrameCount = 0;
                    sinceLastSecond = 0;
            }
        }
    }
    
    private double nanoToSec(long nano) {
        return (double) (nano * Math.pow(10, -9));
    }
    
    // given pixels per second, return the pixels per frame 
    public static final int pps2ppf(double pps) {
        return (int) (pps * (TARGET_UPDATE_FPS));
    }
    
    public long getTime() {
        return System.nanoTime() - initialStartTime;
    }

    public void drawGame(float interpolation) {
        setInterpolation(interpolation);
        repaint();
    }

    public void setInterpolation(float interp) {
        this.interpolation = interp;
    }

    //CALCULATES CURR SCREEN POS & SIZE
    public void calcScreen() {

        if ((getWidth() / 16) <= (getHeight() / 9)) {
            currWidth = (getWidth() / 16) * 16;
            currHeight = 9 * (getWidth() / 16);

        } else {
            currHeight = (getHeight() / 9) * 9;
            currWidth = 16 * (getHeight() / 9);
        }

        screenX = (getWidth() / 2) - (currWidth / 2);
        screenY = (getHeight() / 2) - (currHeight / 2);

//            System.out.println(currWidth + " " + currHeight);
//            currHeight = (getHeight() / 9) * 9;
//            currWidth = 16 * (getHeight() / 9);
    }

    public void update() {

        //input.update();
        //stateMachine.processInput(input);
        calcScreen();
        stateMachine.update();
        
        KeyHandler.update();
//        ControllerHandler.update();
    }
    
    public void update(double deltaTime) {
        System.out.println(deltaTime);
        
        //input.update();
        //stateMachine.processInput(input);
        calcScreen();
        stateMachine.update(deltaTime);
        
        KeyHandler.update();
//        ControllerHandler.update();
    }

    //CALLED FROM PAINTCOMPONENT
    //RENDERS TO G2D IMAGE
    public void render() {

        g2d.setFont(font);

        //RENDER BACKGROUND
        g2d.setColor(new Color(24, 24, 40, 255));
        g2d.fillRect(0, 0, VWIDTH, VHEIGHT);

        //RENDER STATE MACHINE
        if (stateMachine != null) {
            stateMachine.render(g2d);
        }

        frameCount++;
    }

    //CALLED FROM GAME LOOP, RENDERS THE G2D IMAGE ON SCREEN
    //AND SCALES UP TO DESIRED SIZE 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render();

        //g.fillRect(0, 0, VWIDTH, VHEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(renderer, screenX, screenY, currWidth, currHeight, null);

        g.setFont(fpsFont);
        g.setColor(Color.GREEN);
        g.drawString("FPS: " + fps, 5, 45);

        if (time % 60 < 10) {
            g.drawString("" + (time / 60) + ":0" + (time % 60), 5, 60);
        } else {
            g.drawString("" + (time / 60) + ":" + (time % 60), 5, 60);
        }

        //g.fillRect(10, 10, 50, 50);
    }

    @Override
    public void keyTyped(KeyEvent k) {

    }

    @Override
    public void keyPressed(KeyEvent k) {
        //stateMachine.keyPressed(k.getKeyCode());

        KeyHandler.keySet(k.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent k) {
        //stateMachine.keyReleased(k.getKeyCode());
        KeyHandler.keySet(k.getKeyCode(), false);
    }

}
