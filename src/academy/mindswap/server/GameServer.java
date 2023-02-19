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

    public synchronized boolean isMaxPlayerReached() {
        if (playerList.size() == maxPlayersPerGame) {
            return true;
        } else {
            return false;
        }
    }

    public void acceptConnection(int numberOfConnections) throws IOException {
        Socket playerSocket = serverSocket.accept();

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

        if (playerList.size() < maxPlayersPerGame) {
            playerConnectionHandler.send(Messages.WAITING_FOR_OTHER_PLAYERS.formatted(maxPlayersPerGame - playerList.size()));
            this.wait();
        } else this.notifyAll();

    }

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

    public void broadcast(String name, String message) {
        playerList.stream()
                .filter(handler -> !handler.getName().equals(name))
                .forEach(handler -> handler.send(name + ": " + message));
    }


    public void removePlayer(PlayerConnectionHandler playerConnectionHandler) {
        playerList.remove(playerConnectionHandler);
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

        public void quit() {
            hasLeft = true;
            try {
                playerSocket.close();
            } catch (IOException e) {
                System.out.println("Couldn't close player socket");
            }
        }
    }
}
