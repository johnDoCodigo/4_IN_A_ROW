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
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.printf(Messages.SERVER_STARTED, port + "\n");

        gameService = Executors.newCachedThreadPool();
        playerService = Executors.newCachedThreadPool();

        numberOfConnections = 0;

        while (serverSocket.isBound()) {

            while (!isMaxPlayerReached()) {
                acceptConnection(numberOfConnections);
            }

            if (isMaxPlayerReached()) {
                createGame();
                broadcastToAll(Messages.NEW_GAME);
            }
            try { //Give some time to start another game or an error will occur
                Thread.sleep(40);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
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
        Socket playerSocket = serverSocket.accept(); //Blocking method

        PlayerConnectionHandler playerConnectionHandler = new PlayerConnectionHandler(playerSocket, Messages.DEFAULT_NAME + numberOfConnections);
        playerService.submit(playerConnectionHandler);
        playerList.add(playerConnectionHandler);

        System.out.println(Messages.PLAYER_ENTERED_GAME);
    }

    private void createGame() throws RejectedExecutionException {
        ConnectFourHandler game = new ConnectFourHandler(playerList.get(0), playerList.get(1));
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

    private void welcomePlayerAndChangeName(PlayerConnectionHandler playerConnectionHandler) throws IOException {
        //Welcomes the player
        playerConnectionHandler.send(Messages.WELCOME.formatted(playerConnectionHandler.getName()));

        //Asks for name and checks for valid input
        playerConnectionHandler.send(Messages.ASK_NAME);
        playerConnectionHandler.name = playerConnectionHandler.getAnswer();
        while (!playerConnectionHandler.name.matches("[a-zA-Z]+")) {
            playerConnectionHandler.send(Messages.ASK_NAME);
            playerConnectionHandler.name = playerConnectionHandler.getAnswer();
        }

        //Broadcasts the player who joined and presents the command list
        broadcastToAll(String.format(Messages.PLAYER_JOINED, playerConnectionHandler.name));
        playerConnectionHandler.send(Messages.COMMANDS_LIST);
        playerConnectionHandler.send(Messages.WAITING_FOR_OTHER_PLAYERS);
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

    //TODO: to refactor
    public void broadcastToPlayer(String message) {
        playerList.stream()
                .forEach(handler -> handler.send(message));
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

    public String listPlayers() {
        StringBuffer buffer = new StringBuffer();
        playerList.forEach(client -> buffer.append(client.getName()).append("\n"));
        return buffer.toString();
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
        private String name = "";
        private Socket playerSocket;
        private BufferedWriter out;

        //private BufferedReader in;
        Scanner in;
        private String playerInput;
        private int playerTurn;
        private String playerPieceLetter;

        public PlayerConnectionHandler(Socket playerSocket, String name) throws IOException {
            this.playerSocket = playerSocket;
            this.name = name;
            this.out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            //this.in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            this.in = new Scanner(playerSocket.getInputStream());
            /*
            this.playerTurn = getNumberOfConnections();
            if (playerTurn == 2) {
                this.playerPieceLetter = "R";
            }
            if (playerTurn == 1) {
                this.playerPieceLetter = "Y";
            }

             */
        }


        /*
        @Override
        public void run() {
            try {
                if (maxNumberOfPlayers <= 2) {
                    welcomePlayerAndChangeName(this);
                } else {
                    addPlayerToWaitingQueue(this);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                Scanner in = new Scanner(playerSocket.getInputStream());
                while (in.hasNext()) {
                    System.out.println(getName() + "1");
                    System.out.println("player turn: " + playerTurn + " " + playerPieceLetter);
                    if ((connectFourBoard.getNumberOfPlays() % 2 == 0 && (playerTurn % 2 == 0))) {
                        broadcastToPlayer(Messages.WAIT_TURN);
                    }

                    System.out.println(getName() + "2");
                    System.out.println("player turn: " + playerTurn + " " + playerPieceLetter);

                    //User Input & to Int
                    playerChoiceInput = in.nextLine();
                    int playerChoiceInputInt = Integer.parseInt(playerChoiceInput);

                    System.out.println(getName() + "3");
                    System.out.println("player turn: " + playerTurn + " " + playerPieceLetter);

                    //Check for commands
                    if (isCommand(playerChoiceInput)) {
                        dealWithCommand(playerChoiceInput);
                        continue;
                    }

                    System.out.println(getName() + "4");
                    System.out.println("player turn: " + playerTurn + " " + playerPieceLetter);

                    //Check for invalid input
                    while (playerChoiceInputInt < 0 || playerChoiceInputInt > 6) {
                        broadcastToPlayer(Messages.INVALID_COLUMN);
                        playerChoiceInputInt = in.nextInt();
                    }

                    System.out.println(getName() + "5");
                    //Places players choices
                    connectFourBoard.placePiece(Integer.parseInt(playerChoiceInput));
                    System.out.println(getName() + "5.1");

                    broadcastToPlayer(connectFourBoard.getPrettyBoard());
                    System.out.println(connectFourBoard.getPrettyBoard());

                    System.out.println(getName() + "6");
                    //Check for winner
                    if (connectFourBoard.checkWinner(playerPieceLetter)) {
                        connectFourBoard.gameOver(getName()); // gives sound - to check
                        if (playerTurn == 2) {
                            broadcast(getName(), Messages.PLAYER1_WIN);
                        } else {
                            broadcast(getName(), Messages.PLAYER2_WIN);
                        }
                    }

                    System.out.println(getName() + "7");
                    //Check for draw
                    if (connectFourBoard.checkDraw()) {
                        broadcastToPlayer(Messages.CHECK_DRAW);
                        broadcastToPlayer(Messages.PLAY_AGAIN);
                    }

                    System.out.println(getName() + "8");

                }

            } catch (IOException e) {
                System.err.println(Messages.PLAYER_ERROR + e.getMessage());
            } finally {
                removePlayer(this);
            }
        }*/
        @Override
        public void run() {

            try {
                welcomePlayerAndChangeName(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //TODO CHOOSE 1 OR 2 OR 3
            //----------1----------
            /*
            while (!isGameEnded) {
                if (Thread.interrupted()) {
                    return;
                }
            }
             */

            //----------2----------
            while (!playerSocket.isClosed()) {
                try {
                    playerInput = in.nextLine();
                    if (isCommand(playerInput)) {
                        dealWithCommand(playerInput);
                        break;
                    }
                } catch (IOException e) {
                    System.err.println(Messages.PLAYER_ERROR + e.getMessage());
                }
                /*if (!validInput()) {
                    askForGuess();
                }

                 */
                quit();
            }
        }

        public String getAnswer() {
            String message = null;
            try {
                message = in.nextLine();
            } catch (NullPointerException e) {
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

        public String getPlayerInput() {
            return playerInput;
        }

        public void quit() {
            hasLeft = true;
            try {
                playerSocket.close();
            } catch (IOException e) {
                System.out.println("Couldn't closer player socket");
            } finally {
                areStillPlayersPlaying();
                broadcast(String.format(GameMessages.PLAYER_LEFT_GAME, name));
            }
        }
    }

    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public ConnectFourBoard getConnectFour() {
        return connectFourBoard;
    }


}
