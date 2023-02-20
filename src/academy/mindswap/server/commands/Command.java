/*
 * @(#)Command.java        1.0 20/02/2023
 *
 * Copyright (c) MindSwap Academy - João Rodrigues, Filipe Brandão, Rui Rajão e Susana Gandra.
 * All rights reserved.
 *
 * This software was produced for our first group project.
 */

package academy.mindswap.server.commands;

/**
 * Commands that are available to be used by connect four game.
 */
public enum Command {

    //TODO FEATURE
    //WHISPER("/whisper", new WhisperHandler()),
    QUIT("/quit", new QuitHandler()),
    PLAYAGAIN("/playagain", new PlayAgainHandler());

    private String description;
    private CommandHandler handler;

    /**
     * Method constructor of the enum command this accept three arguments
     * @param description represents the name of the command
     * @param handler represents the command that will receive and need to be handler
     */

    Command(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    /**
     * Allows to know what command that will be used
     * @param description represents a String for the command
     * @return Command object, if a matching description is found. Otherwise returns null
     */
    public static Command getCommandFromDescription(String description) {
        for (Command command : values()) {
            if (description.equals(command.description)) {
                return command;
            }
        }
        return null;
    }

    /**
     * Allows to know what command that will be used
     * @return the enum command
     */
    public CommandHandler getHandler() {
        return handler;
    }
}
