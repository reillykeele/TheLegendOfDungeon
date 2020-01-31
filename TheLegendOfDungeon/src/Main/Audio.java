/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author ReillyKeele
 */
public enum Audio {

    //MUSIC
    MENUMUSIC("/Resources/Audio/Music/menuMusic.wav"),
    GAMEMUSIC("/Resources/Audio/Music/gameMusic.wav"),
    //SOUNDS
    MENUMOVE("/Resources/Audio/Sounds/menuMove.wav"),
    MENUSELECT("/Resources/Audio/Sounds/menuSelect.wav"),
    MENUCANCEL("/Resources/Audio/Sounds/menuCancel.wav"),
    GETCOIN("/Resources/Audio/Sounds/getCoin.wav"),
    GETCOIN2("/Resources/Audio/Sounds/getCoin2.wav"),
    GETITEM("/Resources/Audio/Sounds/getItem.wav"),
    ATTACK("/Resources/Audio/Sounds/attack.wav"),
    PLAYERHIT("/Resources/Audio/Sounds/playerHit.wav"),
    ENEMYHIT("/Resources/Audio/Sounds/enemyHit.wav"),
    ENEMYDIE("/Resources/Audio/Sounds/enemyDie.wav"),;

    // Nested class for specifying volume
    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.HIGH;

    // Each sound effect has its own clip, loaded with its own sound file.
    //master clip
    private Clip clip;

    private AudioInputStream audioIn;
    private File file;

    private boolean paused;
    private long position;

    // Constructor to construct each element of the enum with its own sound file.
    Audio(String s) {
        try {

            // Set up an audio input stream piped from the sound file.
            InputStream stream = getClass().getResourceAsStream(s);
            InputStream buff = new BufferedInputStream(stream);
            audioIn = AudioSystem.getAudioInputStream(buff);

            // Get a clip resource.
            clip = AudioSystem.getClip();

//            clip.addLineListener(new CloseClipWhenDone());
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //PLAY SAME SOUND SIMULTANEOUSLY 
//    public void ppplay() {
//        try {
//            
//            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
//            
//            Clip c = AudioSystem.getClip();
//            c.addLineListener(new CloseClipWhenDone());
//            c.open(audioIn);
//            
//            c.setFramePosition(0); // rewind to the beginning
//            c.start();     // Start playing   
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    // Play or Re-play the sound effect from the beginning, by rewinding.
    public void play(Boolean loop) {
        if (!Settings.MUTED.isTrue()) {
            paused = false;

            if (volume != Volume.MUTE) {
                if (clip.isRunning()) {
                    clip.stop();   // Stop the player if it is still running
                }
                clip.setFramePosition(0); // rewind to the beginning            
                clip.start();     // Start playing
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        }
    }

    public void stop() //stop playing and rewind to be played again from the beginning
    {
        clip.stop();
        clip.setFramePosition(0);
    }

    public void pause() {
        position = clip.getMicrosecondPosition();
        clip.stop();

        paused = true;
    }

    public void resume() {
        if (!Settings.MUTED.isTrue()) {
            clip.setMicrosecondPosition(position);
            clip.start();

            clip.loop(Clip.LOOP_CONTINUOUSLY);

            paused = false;
        }
    }

    public boolean paused() {
        return paused;
    }

    public void mute() //don't play sounds(Mute Sound is selected from Options menu)
    {
        volume = Volume.MUTE;
    }
}

//class CloseClipWhenDone implements LineListener {
//
//    @Override
//    public void update(LineEvent event) {
//        if (event.getType().equals(LineEvent.Type.STOP)) {
//            Line soundClip = event.getLine();
//            soundClip.close();
//
//            //Just to prove that it is called...
//            //System.out.println("Done playing " + soundClip.toString());
//        }
//    }
//
//}
