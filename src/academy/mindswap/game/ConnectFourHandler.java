package academy.mindswap.game;

import academy.mindswap.server.GameServer;
import academy.mindswap.server.messages.Messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectFourHandler implements Runnable {

    private final GameServer.PlayerConnectionHandler player1;
    private final GameServer.PlayerConnectionHandler player2;
    private final ConnectFourBoard board;
    private String move;
    public static volatile boolean isGameEnded;
    private volatile boolean isGameStarted;
    private boolean checkIfGameCanStart = false;

    private List<GameServer.PlayerConnectionHandler> listOfBoardPlayers = new ArrayList<>();

    public ConnectFourHandler(GameServer.PlayerConnectionHandler player1, GameServer.PlayerConnectionHandler player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new ConnectFourBoard();
        this.move = "R";
        isGameEnded = false;
        isGameStarted = false;
        listOfBoardPlayers.add(player1);
        listOfBoardPlayers.add(player2);
    }


    @Override
    public void run() {
        GameServer.PlayerConnectionHandler currentPlayer = player1;
        GameServer.PlayerConnectionHandler notPlayingPlayer = player2;

        ReentrantLock lockNotYourTurnInput = new ReentrantLock();

        //Waits a bit so that players can read the instructions.
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while (!isGameEnded) {
            if (checkIfGameCanStart() && !isGameStarted) {
                startGame();
            }

            if (isGameStarted && !isGameEnded) {
                //Broadcasts current player turn
                lockNotYourTurnInput.lock();
                currentPlayer.send(currentPlayer.getName() + ", it's your turn!");
                notPlayingPlayer.send(notPlayingPlayer.getName() + ", you must wait for your turn.");
                lockNotYourTurnInput.unlock();

                //Get player turn input and checks from valid input
                Integer playerMove = null;
                while (playerMove == null) {
                    currentPlayer.send("Enter a number between 0 and 6:");
                    String input = null;
                    try {
                        input = currentPlayer.in.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (input.matches("[0-6]")) {
                        playerMove = Integer.parseInt(input);
                    } else {
                        currentPlayer.send("Invalid input.");
                    }
                }

                //Place current player move
                board.placePiece(getMove(), playerMove);

                //Checks for winner
                if (board.checkWinner(getMove())) {
                    broadcast(currentPlayer.getName() + " HAS WON THE GAME!");
                    broadcast(board.getPrettyBoard());
                    if (currentPlayer == player1) {
                        broadcast(Messages.PLAYER1_WIN);
                    } else {
                        broadcast(Messages.PLAYER2_WIN);
                    }
                    break;
                }

                //Checks for draw
                if (board.checkDraw()) {
                    broadcast(board.getPrettyBoard());
                    broadcast(Messages.CHECK_DRAW);
                    break;
                }

                //Broadcasts the board with the new move
                broadcast(board.getPrettyBoard());

                //Switch move and player.
                switchMove();
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
                notPlayingPlayer = (notPlayingPlayer == player2) ? player1 : player2;
            }
        }
        //TODO: Clean up or playagain
        broadcast(Messages.PLAY_AGAIN_OR_QUIT);
    }

    public void startGame() {
        isGameStarted = true;
        broadcast(Messages.START_GAME);
        broadcast(board.getPrettyBoard());
    }

    public synchronized void broadcast(String message) {
        listOfBoardPlayers.stream()
                .forEach(player -> player.send(message));
    }


    private synchronized boolean checkIfGameCanStart() {
        return checkIfGameCanStart;
    }

    public synchronized boolean theGameCanStart() {
        this.checkIfGameCanStart = true;
        return true;
    }

    public String getMove() {
        return move;
    }

    public void switchMove() {
        move = (move == "R") ? "Y" : "R";
    }

}
