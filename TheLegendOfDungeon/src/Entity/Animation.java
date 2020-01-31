package Entity;

import Main.GamePanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Animation {

    private BufferedImage spriteSheet;

    private int delay;      //every x counts
    private int count;      //count
    private int currFrame;
    private int numFrames;

    private boolean stopped;

    private boolean animated;
    private boolean loop;
    private boolean finished;

    private int width;
    private int height;

    private BufferedImage[] frames;

    public Animation(String file, int delay, int numFrames, int width, int height, boolean animated, boolean loop) {

        try {
            stopped = true;
            this.delay = delay;
            this.numFrames = numFrames;
            this.height = height;
            this.width = width;
            this.loop = loop;

            InputStream stream = getClass().getResourceAsStream(file);
            spriteSheet = ImageIO.read(stream);
            frames = new BufferedImage[numFrames];

            for (int i = 0; i < spriteSheet.getWidth() / width; i++) {
                frames[i] = (spriteSheet.getSubimage(i * width, 0, width, height));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private long frameLength;
    private long lastFrame;

    public Animation(String file, long frameLength, int numFrames, int width, int height, boolean animated, boolean loop) {

        try {
            stopped = true;
            this.frameLength = frameLength;
            this.numFrames = numFrames;
            this.height = height;
            this.width = width;
            this.animated = animated;
            this.loop = loop;

            InputStream stream = getClass().getResourceAsStream(file);
            spriteSheet = ImageIO.read(stream);
            frames = new BufferedImage[numFrames];

            for (int i = 0; i < spriteSheet.getWidth() / width; i++) {
                frames[i] = (spriteSheet.getSubimage(i * (width), 0, (width), height));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean getFinished() {
        return finished;
    }

    public void start() {
        if (!stopped) {
            return;
        }

        currFrame = 0;
        stopped = false;

        if (!loop) {
            finished = false;
        }
    }

    public void stop() {
        currFrame = numFrames - 1;
        
        stopped = true;
        if (!loop) {
            finished = true;
        }
    }

    public int getCurrFrame() {
        return currFrame;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getLoop() {
        return loop;
    }

    public void setFrame(int frame) {
        if (frame < numFrames) {
            this.currFrame = frame;
        }
    }

    public BufferedImage getSprite() {
        try {
            return frames[currFrame];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void update3() {

        //IF ONLY TO BE PLAYED ONCE, WHEN CALLING UPDATE() :
        //
        //        if (!animations[currAnimation].getFinished()) {
        //            setAnimation(X);
        //            animations[currAnimation].update();
        //        }
        //
        if (delay != 0) {
            if (!stopped && !finished) {
                count++;

                if (count % delay == 0) {
                    currFrame++;
                    if (currFrame > frames.length - 1) {
                        currFrame = 0;

                        if (!loop) {
                            stop();
                            System.out.println(finished);
                        }

                    }
                }

            }
        }
    }

    public void update() {

        if (animated && !stopped && !finished) {

            if (System.nanoTime() - lastFrame >= frameLength) {

                currFrame++;

                if (currFrame > frames.length - 1) {

                    if (!loop) {
                        stop();
                    } else {
                        currFrame = 0;
                    }
                }
                lastFrame = System.nanoTime();
            }

        }
    }

}
