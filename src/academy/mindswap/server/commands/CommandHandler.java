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

/**
 * All the commands implement the interface command handler
 */
public interface CommandHandler {
    /**
     *This method represent the choice that each player can take
     * @param gameServer represent the instance of a member class GameServer
     * @param playerConnectionHandler access the properties and methods for player connection
     */
    void execute(GameServer gameServer, GameServer.PlayerConnectionHandler playerConnectionHandler);
}
