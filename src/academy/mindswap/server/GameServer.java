/*
 * @(#)Command.java        1.0 20/02/2023
 *
 * Copyright (c) MindSwap Academy - João Rodrigues, Filipe Brandão, Rui Rajão e Susana Gandra.
 * All rights reserved.
 *
 * This software was produced for our first group project.
 */
package academy.mindswap.server;
import academy.mindswap.game.ConnectFourBoard;
import academy.mindswap.game.ConnectFourHandler;
import academy.mindswap.server.commands.Command;
import academy.mindswap.server.messages.Messages;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Implements the connection between the GameServer and the players (or two machines)
 */

public class GameServer {
    private ServerSocket serverSocket;
    private ExecutorService gameService;
    private ExecutorService playerService;
    private final List<PlayerConnectionHandler> playerList;
    private final List<ConnectFourHandler> gameList;
    private ConnectFourBoard connectFourBoard;
    private int maxPlayersPerGame = 2;
    private int numberOfConnections;

    //TODO FEATURE private final List<playerConnectionHandler> playersWaitingQueue;

    /**
     * Constructor method to initialize the properties
     */
    public GameServer() {
        gameList = new CopyOnWriteArrayList<>();
        playerList = new CopyOnWriteArrayList<>();
        numberOfConnections = 0;
    }

    /**
     * @param port starts the game in a specified port
     * Creates a new thread for the gameService
     * Creates a new thread for the playerService
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.printf(Messages.SERVER_STARTED, port + "\n");
        System.out.println("Server step 1");

        gameService = Executors.newCachedThreadPool();
        playerService = Executors.newCachedThreadPool();

        while (playerList.size() < maxPlayersPerGame) {
            while (!isMaxPlayerReached()) {
                acceptConnection(numberOfConnections);
                numberOfConnections++;
                System.out.println("Server step 2");
                System.out.println(playerList.size());
            }

            if (playerList.size() == 2) {
                createGame();
                System.out.println("Server step 3");
            }
        }
    }

    /**
     * Check is the max number of player per game is reached
     * @return true if the condition is verified
     */
    public synchronized boolean isMaxPlayerReached() {
        if (playerList.size() == maxPlayersPerGame) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Accept the connection of the players to the GameServer
     * @param numberOfConnections for the players
     * @throws IOException if the number of connection ir over two
     */
    public void acceptConnection(int numberOfConnections) throws IOException {
        Socket playerSocket = serverSocket.accept();

        PlayerConnectionHandler playerConnectionHandler = new PlayerConnectionHandler(playerSocket, Messages.DEFAULT_NAME + numberOfConnections);
        playerService.submit(playerConnectionHandler);

        System.out.println(playerConnectionHandler.getName() + Messages.PLAYER_ENTERED_GAME);
    }

    /**
     * Adds the player into the playerList
     * Prints to the console a welcome messages to the players
     * Asks for players names and checks for a valid input
     * @param playerConnectionHandler
     * @throws IOException
     * @throws InterruptedException
     */
    private synchronized void addPlayer(PlayerConnectionHandler playerConnectionHandler) throws IOException, InterruptedException {
        playerList.add(playerConnectionHandler);
        playerConnectionHandler.send(Messages.WELCOME.formatted(playerConnectionHandler.getName()));

        playerConnectionHandler.send(Messages.ASK_NAME);
        playerConnectionHandler.name = playerConnectionHandler.getAnswer();

        if (playerList.size() < maxPlayersPerGame) {
            playerConnectionHandler.send(Messages.WAITING_FOR_OTHER_PLAYERS.formatted(maxPlayersPerGame - playerList.size()));
            this.wait();
        } else this.notifyAll();

    }

    /**
     * Creates a ConnectFourHandler object, and starts connect four game adding them to de game list
     * @throws RejectedExecutionException is the game does not star
     */
    private void createGame() throws RejectedExecutionException {
        ConnectFourHandler game = new ConnectFourHandler(playerList.get(0), playerList.get(1));
        game.theGameCanStart();
        gameList.add(game);
        gameService.execute(game);
    }

    //TODO FEATURE:
    /*
    private void addPlayerToWaitingQueue(playerConnectionHandler playerConnectionHandler) throws IOException {
        playersWaitingQueue.add(playerConnectionHandler);
        playerConnectionHandler.send(Messages.WAITING_QUEUE.formatted(playerConnectionHandler.getName()));


        //To refactor
        String newName = getPlayerNameInput(playerConnectionHandler.playerSocket);
        playerConnectionHandler.setName(newName);
        System.out.println(playerConnectionHandler.getName());

        playerConnectionHandler.send(Messages.COMMANDS_LIST);
        broadcast(playerConnectionHandler.getName(), Messages.PLAYER_ENTERED_GAME);

    }
    */

    /**
     * If the connection between player and gameServer breaks removes the player from the list
     * @param playerConnectionHandler
     */
    public void removePlayer(PlayerConnectionHandler playerConnectionHandler) {
        playerList.remove(playerConnectionHandler);
    }

    /**
     * Inner class that implements Runnable
     */
    public class PlayerConnectionHandler implements Runnable {
        ConnectFourHandler connectFourHandler;
        private String name;
        private Socket playerSocket;
        public BufferedWriter out;
        public BufferedReader in;
        private int playerInput;
        private boolean hasLeft;

        /**
         * Constructor method
         * @param playerSocket channel of communication between the player and GameServer
         * @param name String for players input
         * @throws IOException when the communication is affect
         */
        public PlayerConnectionHandler(Socket playerSocket, String name) throws IOException {
            this.playerSocket = playerSocket;
            this.name = name;
            this.out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

        }

        /**
         * Starts the communication between the player and GameServe
         */
        @Override
        public void run() {
            try {
                addPlayer(this);
                send(Messages.INSTRUCTIONS);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            while (!connectFourHandler.isGameEnded) {
                if (Thread.interrupted()) {
                    return;
                }
            }
            quit();
        }

        /**
         * Read the message from the player written in the console
         * throws IOException If message is null
         * @return message
         */
        public String getAnswer() {
            String message = null;
            try {
                message = in.readLine();
            } catch (IOException | NullPointerException e) {
                quit();
            } finally {
                if (message == null) {
                    quit();
                }
            }
            return message;
        }

        //TODO FEATURE
        private boolean isCommand(String message) {
            return message.startsWith("/");
        }

        //TODO FEATURE
        public void dealWithCommand(String message) throws IOException {
            String description = message.split(" ")[0];
            Command command = Command.getCommandFromDescription(description);

            if (command == null) {
                out.write(Messages.NO_SUCH_COMMAND);
                out.newLine();
                out.flush();
                return;
            }
            command.getHandler().execute(GameServer.this, this);
        }

        /**
         * Permits GameServer to send a messages to the players.
         * @param message sent by GameServer to the player
         */
        public void send(String message) {
            try {
                out.write(message);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                removePlayer(this);
                e.printStackTrace();
            }
        }
        /**
         * After the server close the socket, the player socket will close and left the game
         */
        public void close() {
            try {
                playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Access players name in other classes
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * If the player lefts connect four game, players socket will close
         */
        public void quit() {
            hasLeft = true;
            try {
                playerSocket.close();
            } catch (IOException e) {
                System.out.println("Couldn't closer player socket");
            }
        }
    }
}
