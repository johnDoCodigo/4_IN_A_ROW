package academy.mindswap.server.sounds;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    Clip soundLoop;
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

}