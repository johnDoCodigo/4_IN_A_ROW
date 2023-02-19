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

    public GameServer() {
        gameList = new CopyOnWriteArrayList<>();
        playerList = new CopyOnWriteArrayList<>();
        numberOfConnections = 0;
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.printf(Messages.SERVER_STARTED, port + "\n");

        gameService = Executors.newCachedThreadPool();
        playerService = Executors.newCachedThreadPool();

        System.out.println("Server step 1");


        while (playerList.size() < maxPlayersPerGame) {
            System.out.println("Server step 2");


            while (!isMaxPlayerReached()) {
                acceptConnection(numberOfConnections);
                numberOfConnections++;
                System.out.println(playerList.size());
                System.out.println("Server step 3");
            }

            if (playerList.size() == 2) {
                createGame();
                System.out.println("Server step 5");

            }

            System.out.println("Server step 6");

        }
    }

    public synchronized boolean isMaxPlayerReached() {
        if (playerList.size() == maxPlayersPerGame) {
            return true;
        } else {
            return false;
        }
    }

    public void acceptConnection(int numberOfConnections) throws IOException {
        Socket playerSocket = serverSocket.accept(); //Blocking method

        PlayerConnectionHandler playerConnectionHandler = new PlayerConnectionHandler(playerSocket, Messages.DEFAULT_NAME + numberOfConnections);
        playerService.submit(playerConnectionHandler);

        System.out.println(playerConnectionHandler.getName() + Messages.PLAYER_ENTERED_GAME);
    }

    private synchronized void addPlayer(PlayerConnectionHandler playerConnectionHandler) throws IOException, InterruptedException {
        //Adds the player to the list
        playerList.add(playerConnectionHandler);

        //Welcomes the player
        playerConnectionHandler.send(Messages.WELCOME.formatted(playerConnectionHandler.getName()));

        //Asks for name and checks for valid input
        playerConnectionHandler.send(Messages.ASK_NAME);
        playerConnectionHandler.name = playerConnectionHandler.getAnswer();

        /*
        while (!playerConnectionHandler.name.matches("[a-zA-Z]+")) {
            playerConnectionHandler.send(Messages.ASK_NAME);
            playerConnectionHandler.name = playerConnectionHandler.getAnswer();
        }

         */

        if (playerList.size() < maxPlayersPerGame) {
            playerConnectionHandler.send(Messages.WAITING_FOR_OTHER_PLAYERS.formatted(maxPlayersPerGame - playerList.size()));
            this.wait();
        } else this.notifyAll();

        /*
        //Broadcasts the player who joined and presents the command list
        broadcastToAll(String.format(Messages.PLAYER_JOINED, playerConnectionHandler.name));
        playerConnectionHandler.send(Messages.COMMANDS_LIST);
        playerConnectionHandler.send(Messages.WAITING_FOR_OTHER_PLAYERS);
         */

    }

    private void createGame() throws RejectedExecutionException {
        ConnectFourHandler game = new ConnectFourHandler(playerList.get(0), playerList.get(1));
        game.theGameCanStart();
        gameList.add(game);
        gameService.execute(game);
    }

    private String getPlayerNameInput(Socket playerSocket) throws IOException {
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(playerSocket.getInputStream())); //reads input from the input stream of the clientSocket object, which represents the client's connection to the server.
        BufferedWriter outputName = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream())); //writes output to the output stream of the clientSocket object, which represents the client's connection to the server.
        outputName.write("Please insert your username"); //writes the message "Please insert your username" to the client through the output stream
        outputName.newLine(); //Add a newline character to the output stream
        outputName.flush(); //flush the buffer. This ensures that the message is sent to the client immediately.
        return consoleInput.readLine(); //returns the client username
    }



    /* //TODO FEATURE AND REFACTOR
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

    //TODO: to refactor
    public void broadcast(String name, String message) {
        playerList.stream()
                .filter(handler -> !handler.getName().equals(name))
                .forEach(handler -> handler.send(name + ": " + message));
    }

    public synchronized void broadcastToAll(String message) {
        playerList.stream()
                .forEach(player -> player.send(message));
    }

    public synchronized void broadcastToOther(String message, PlayerConnectionHandler doNotBroadcast) {
        playerList.stream()
                .filter(p -> !p.equals(doNotBroadcast))
                .forEach(player -> player.send(message));
    }

    public synchronized void broadCastToHimself(String message, PlayerConnectionHandler himself) {
        playerList.stream()
                .filter(p -> p.equals(himself))
                .forEach(player -> player.send(message));
    }

    public void removePlayer(PlayerConnectionHandler playerConnectionHandler) {
        playerList.remove(playerConnectionHandler);

    }

    public Optional<PlayerConnectionHandler> getClientByName(String name) {
        return playerList.stream()
                .filter(clientConnectionHandler -> clientConnectionHandler.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public class PlayerConnectionHandler implements Runnable {
        ConnectFourHandler connectFourHandler;
        private String name;
        private Socket playerSocket;
        public BufferedWriter out;
        public BufferedReader in;
        private int playerInput;
        private boolean hasLeft;

        public PlayerConnectionHandler(Socket playerSocket, String name) throws IOException {
            this.playerSocket = playerSocket;
            this.name = name;
            this.out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

        }

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


        private boolean isCommand(String message) {
            return message.startsWith("/");
        }

        private void dealWithCommand(String message) throws IOException {
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

        public void close() {
            try {
                playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPlayerInput() {
            return playerInput;
        }

        public void quit() {
            hasLeft = true;
            try {
                playerSocket.close();
            } catch (IOException e) {
                System.out.println("Couldn't closer player socket");
            } finally {
                //areStillPlayersPlaying();
                //broadcastToAll(String.format(Messages.PLAYER_LEFT_GAME, name));
            }
        }
    }

    /*
    public void areStillPlayersPlaying() {
        if (listOfPlayers.stream()
                .filter(p -> p.hasLeft).count() == MAX_NUM_OF_PLAYERS) {
            endGame();
        }
    }
     */


    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public ConnectFourBoard getConnectFour() {
        return connectFourBoard;
    }


}
