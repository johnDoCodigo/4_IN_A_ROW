package academy.mindswap.server;

import academy.mindswap.game.ConnectFour;
import academy.mindswap.server.commands.Command;
import academy.mindswap.server.messages.Messages;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private ServerSocket serverSocket;
    private ExecutorService service;
    private final List<playerConnectionHandler> players;
    private ConnectFour connectFour;
    private int numberOfConnections;
    private int maxNumberOfPlayers = 0;
    private final List<playerConnectionHandler> playersWaitingQueue;

    public GameServer() {
        players = new CopyOnWriteArrayList<>();
        playersWaitingQueue = new CopyOnWriteArrayList<>();
        connectFour = new ConnectFour();
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        service = Executors.newCachedThreadPool();
        numberOfConnections = 10;

       // System.out.printf(Messages.SERVER_STARTED, port + "\n");

        while (serverSocket.isBound()) {
            acceptConnection(numberOfConnections); //Blocking method
            ++numberOfConnections;
            maxNumberOfPlayers++;
        }
        /*while (maxNumberOfPlayers >= 2) {
            acceptConnection(numberOfConnections); //Blocking method
            ++numberOfConnections;
            maxNumberOfPlayers++;
        }*/

    }

    public void acceptConnection(int numberOfConnections) throws IOException {
        Socket playerSocket = serverSocket.accept(); //Blocking method
        playerConnectionHandler playerConnectionHandler =
                new playerConnectionHandler(playerSocket,
                        Messages.DEFAULT_NAME + numberOfConnections);
        service.submit(playerConnectionHandler);
    }

    private String getPlayerNameInput(Socket playerSocket) throws IOException {
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(playerSocket.getInputStream())); //reads input from the input stream of the clientSocket object, which represents the client's connection to the server.
        BufferedWriter outputName = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream())); //writes output to the output stream of the clientSocket object, which represents the client's connection to the server.
        outputName.write("Please insert your username"); //writes the message "Please insert your username" to the client through the output stream
        outputName.newLine(); //Add a newline character to the output stream
        outputName.flush(); //flush the buffer. This ensures that the message is sent to the client immediately.
        return consoleInput.readLine(); //returns the client username
    }

    private void addPlayer(playerConnectionHandler playerConnectionHandler) throws IOException {
        players.add(playerConnectionHandler);
        playerConnectionHandler.send(Messages.WELCOME.formatted(playerConnectionHandler.getName()));

        //To refactor
        String newName = getPlayerNameInput(playerConnectionHandler.playerSocket);
        playerConnectionHandler.setName(newName);
        System.out.println(playerConnectionHandler.getName());

        playerConnectionHandler.send(Messages.COMMANDS_LIST);
        broadcast(Messages.PLAYER_ENTERED_GAME);
        //broadcast(playerConnectionHandler.getName(), Messages.PLAYER_ENTERED_GAME);
    }

    private void addPlayerToWaitingQueue(playerConnectionHandler playerConnectionHandler) throws IOException {
        playersWaitingQueue.add(playerConnectionHandler);
        playerConnectionHandler.send(Messages.WAITING_QUEUE.formatted(playerConnectionHandler.getName()));

        //TODO FEATURE AND REFACTOR
        /*
        //To refactor
        String newName = getPlayerNameInput(playerConnectionHandler.playerSocket);
        playerConnectionHandler.setName(newName);
        System.out.println(playerConnectionHandler.getName());

        playerConnectionHandler.send(Messages.COMMANDS_LIST);
        broadcast(playerConnectionHandler.getName(), Messages.PLAYER_ENTERED_GAME);
         */
    }

    public synchronized void broadcast(String message) {
        players.stream()
                .forEach(handler -> handler.send(message));
    }

    public synchronized void broadcast(String message, playerConnectionHandler doNotBroadcast){
        players.stream()
            .filter(p -> !p.equals(doNotBroadcast))
            .forEach(player -> player.send(message));
    }


    public String listPlayers() {
        StringBuffer buffer = new StringBuffer();
        players.forEach(client -> buffer.append(client.getName()).append("\n"));
        return buffer.toString();
    }

    public void removePlayer(playerConnectionHandler playerConnectionHandler) {
        players.remove(playerConnectionHandler);

    }

    public Optional<playerConnectionHandler> getClientByName(String name) {
        return players.stream()
                .filter(clientConnectionHandler -> clientConnectionHandler.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public class playerConnectionHandler implements Runnable {
        private String name;
        private Socket playerSocket;
        private BufferedWriter out;
        private String playerChoiceInput;
        private int playerTurn;
        private String playerPieceLetter;

        public playerConnectionHandler(Socket playerSocket, String name) throws IOException {
            this.playerSocket = playerSocket;
            this.name = name;
            this.out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            this.playerTurn = getNumberOfConnections();

            if (playerTurn == 2) {
                this.playerPieceLetter = "R";
            }
            if (playerTurn == 1) {
                this.playerPieceLetter = "Y";
            }
        }

        @Override
        public void run() {
            try {
                if (maxNumberOfPlayers <= 2) {
                    addPlayer(this);
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
                    System.out.println("player turn: " + playerTurn + playerPieceLetter);
                    synchronized (this) {
                        if ((connectFour.getNumberOfPlays() % 2 == 0)) {
                            broadcast(String.format(Messages.WAIT_TURN, name));
                        }
                    }

                    System.out.println(getName() + "2");
                    System.out.println("player turn: " + playerTurn + playerPieceLetter);

                    //User Input & to Int
                    playerChoiceInput = in.nextLine();
                    int playerChoiceInputInt = Integer.parseInt(playerChoiceInput);

                    System.out.println(getName() + "3");
                    System.out.println("player turn: " + playerTurn + playerPieceLetter);

                    //Check for commands
                    if (isCommand(playerChoiceInput)) {
                        dealWithCommand(playerChoiceInput);
                        continue;
                    }

                    System.out.println(getName() + "4");
                    System.out.println("player turn: " + playerTurn + playerPieceLetter);

                    //Check for invalid input
                    while (playerChoiceInputInt < 0 || playerChoiceInputInt > 6) {
                        broadcast(String.format(Messages.INVALID_COLUMN, name));
                        playerChoiceInputInt = in.nextInt();
                    }

                    System.out.println(getName() + "5");

                    //Places players choices
                    connectFour.placePiece(Integer.parseInt(playerChoiceInput));
                    System.out.println(getName() + "5.1");

                    broadcast(String.format(connectFour.getPrettyBoard()));
                    System.out.println(connectFour.getPrettyBoard());

                    System.out.println(getName() + "6");


                    //Check for winner
                    if (connectFour.checkWinner(playerPieceLetter)) {
                        connectFour.gameOver(getName()); // gives sound - to check
                        if (playerTurn == 2) {
                            broadcast(Messages.PLAYER1_WIN);
                        } else {
                            broadcast(Messages.PLAYER2_WIN);
                        }
                    }

                    System.out.println(getName() + "7");
                    //Check for draw
                    if (connectFour.checkDraw()) {
                        broadcast(String.format(Messages.CHECK_DRAW));
                        broadcast(String.format(Messages.PLAY_AGAIN, name));
                       // broadcastToPlayer(Messages.CHECK_DRAW);
                        //broadcastToPlayer(Messages.PLAY_AGAIN);
                    }

                    System.out.println(getName() + "8");

                }

            } catch (IOException e) {
                System.err.println(Messages.PLAYER_ERROR + e.getMessage());
            } finally {
                removePlayer(this);
            }
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

        public String getPlayerChoiceInput() {
            return playerChoiceInput;
        }
    }

    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public ConnectFour getConnectFour() {
        return connectFour;
    }
}
