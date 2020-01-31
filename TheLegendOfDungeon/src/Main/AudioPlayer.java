/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 *
 * @author ReillyKeele
 */
public class AudioPlayer implements LineListener {

    private Clip clip;
    private boolean loop;
    
    public static Audio audio;

    public AudioPlayer(String s, boolean loop) {

        try {

            this.loop = loop;

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(s));
            // Get a sound clip resource.
            clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            //clip.addLineListener(new CloseClipWhenDone());
        } catch (Exception e) {

        }

        //AudioSystem ai = AudioSystem.getAudioFileFormat(new File("src/Resources/Audio/Music/menuMusic.wav"));
    }
    
    public static void playYeet() {
        try {
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            
//            clip.stop();
//            clip.setFramePosition(0);
//
//            clip.start();

            playYeet();

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
    }

    public void stop() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
                clip.setFramePosition(0);
            }
        }
    }

    @Override
    public void update(LineEvent event) {

    }

}

//class CloseClipWhenDone implements LineListener {
//
//    @Override
//    public void update(LineEvent event) {
//        if (event.getType().equals(LineEvent.Type.STOP))
//        {
//            Line soundClip = event.getLine();
//            soundClip.close();
//             
//            //Just to prove that it is called...
//            //System.out.println("Done playing " + soundClip.toString());
//        }
//    }
//    
//}
