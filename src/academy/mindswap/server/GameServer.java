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

public class GameServer{
    private ServerSocket serverSocket;
    private ExecutorService service;
    private final List<playerConnectionHandler> players;
    private ConnectFour connectFour;

    private int numberOfConnections;


    public GameServer() {
        players = new CopyOnWriteArrayList<>();
        connectFour = new ConnectFour();
    }


    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        service = Executors.newCachedThreadPool();
        numberOfConnections = 1;
        System.out.printf(Messages.SERVER_STARTED, port + "\n");

        while (true) {
            //TODO: Accept only 2 connections //RUI
            acceptConnection(numberOfConnections); //Blocking method
            ++numberOfConnections;
        }

    }

    public void acceptConnection(int numberOfConnections) throws IOException {
        Socket playerSocket = serverSocket.accept(); //Blocking method
        playerConnectionHandler playerConnectionHandler =
                new playerConnectionHandler(playerSocket,
                Messages.DEFAULT_NAME + numberOfConnections);
        service.submit(playerConnectionHandler);
    }

    private String getPlayerNameInput(Socket playerSocket)  throws IOException {
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
        broadcast(playerConnectionHandler.getName(), Messages.PLAYER_ENTERED_GAME);

        //TODO Faz o print da lista de comandos duas vezes no mesmo player, uma quando o player coloca o nome outra quando o segundo cinsere o nome;

    }

    public void broadcast(String name, String message) {
        players.stream()
                .filter(handler -> !handler.getName().equals(name))
                .forEach(handler -> handler.send(name + ": " + message));
    }

    public void broadcastToPlayer(String message) {
        players.stream()
                .forEach(handler -> handler.send(message));
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
        private static BufferedWriter out;
        private String playerChoiceInput;

        private int playerTurn;

        public playerConnectionHandler(Socket playerSocket, String name) throws IOException {
            this.playerSocket = playerSocket;
            this.name = name;
            this.out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            this.playerTurn = getNumberOfConnections();
        }

        @Override
        public void run() {

            try {
                addPlayer(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {

                Scanner in = new Scanner(playerSocket.getInputStream());
                while (in.hasNext()) {

                    //Todo -> if player turn, continue, else, wait.
                    synchronized (this) {
                        if (connectFour.getNumberOfPlays() % (this.playerTurn) == 0 ) {
                            continue;
                        } else {
                            wait();
                            notifyAll();
                        }
                    }
                        playerChoiceInput = in.nextLine();


                    //TODO filter the input from the player - he can only input 0-6. (regex)

                    //TODO playerChoiceInput -> connectFour.placePiece(playerChoiceInput)...

                    //TODO checkWinner
                    if (true/*connectFour.checkWinner(this)*/){
                        //broadcast MESSAGE.WINNER
                        //broadcast prettyBoard with winner
                        //if ... command wants to play again ? resetBoard : socketCloses; BOTH PLAYERS MUST ACCEPT TO PLAYAGAIN
                    }

                    //TODO checkDraw
                    synchronized (this) {
                        if (connectFour.checkDraw()) {
                            connectFour.getPrettyBoard();
                            broadcastToPlayer(Messages.CHECK_DRAW);
                        }
                        if (isCommand(playerChoiceInput)) {
                            dealWithCommand(playerChoiceInput);
                            continue;
                        }
                    }

                    broadcast(name, connectFour.getPrettyBoard());
                    notifyAll();
                }

            } catch (IOException e) {
                System.err.println(Messages.PLAYER_ERROR + e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
}
