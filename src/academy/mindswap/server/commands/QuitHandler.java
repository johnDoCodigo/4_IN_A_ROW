/*
 * @(#)Command.java        1.0 20/02/2023
 *
 * Copyright (c) MindSwap Academy - João Rodrigues, Filipe Brandão, Rui Rajão e Susana Gandra.
 * All rights reserved.
 *
 * This software was produced for our first group project.
 */

package academy.mindswap.server.commands;

import academy.mindswap.server.GameServer;
import academy.mindswap.server.messages.Messages;

/**
 * When the game is over the player can choose if he wants to quit the game and close their connection
 */

public class QuitHandler implements CommandHandler {

    /**
     * @param gameServer represent the instance of a member class GameServer
     * @param playerConnectionHandler access the properties and methods for player connection
     *  */

    @Override
    public void execute(GameServer gameServer, GameServer.PlayerConnectionHandler playerConnectionHandler) {
        gameServer.removePlayer(playerConnectionHandler);
        playerConnectionHandler.close();
    }
}