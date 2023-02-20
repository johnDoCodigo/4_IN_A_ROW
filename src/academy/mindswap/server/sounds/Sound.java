/*
 * @(#)Command.java        1.0 20/02/2023
 *
 * Copyright (c) MindSwap Academy - João Rodrigues, Filipe Brandão, Rui Rajão e Susana Gandra.
 * All rights reserved.
 *
 * This software was produced for our first group project.
 */
package academy.mindswap.server.sounds;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Class responsible to sent a sound effect for connect four game
 */


public class Sound {
    Clip soundLoop;

    /**
     * Gets a sound clip from a file and plays it once.
     *
     * @param soundPath The path to the sound file.
     * @throws RuntimeException if the sound file is not supported or if there is an error playing the sound.
     */
    public void getSoundClip(String soundPath) {
        final Clip soundClip;
        try {
            File soundFile = new File(soundPath);
            AudioInputStream soundToPlay = AudioSystem.getAudioInputStream(soundFile);
            soundClip = AudioSystem.getClip();
            soundClip.open(soundToPlay);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        if (soundClip.isRunning()) soundClip.stop();   // Stop the player if it is still running
        soundClip.setFramePosition(0); // rewind to the beginning
        soundClip.start();     // Start playing
    }

    public Clip getSoundLoopVar() {
        return soundLoop;
    }
}