/*
 * @(#)Command.java        1.0 20/02/2023
 *
 * Copyright (c) MindSwap Academy - João Rodrigues, Filipe Brandão, Rui Rajão e Susana Gandra.
 * All rights reserved.
 *
 * This software was produced for our first group project.
 */
package academy.mindswap.server.sounds;

/**
 * Sounds sent to player by connect four game when is playing the game
 */
public enum SoundFiles {

    GAME_OVER("sounds/GAME_OVER.wav"),
    PLAYER1_PIECE("sounds/PLAYER1_PIECE.wav"),
    PLAYER2_PIECE("sounds/PLAYER2_PIECE.wav");
    private final String path;

    SoundFiles(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}
