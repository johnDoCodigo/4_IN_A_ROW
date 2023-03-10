package academy.mindswap.game;

import academy.mindswap.server.GameServer;
import academy.mindswap.server.messages.Messages;
import academy.mindswap.server.sounds.Sound;
import academy.mindswap.server.sounds.SoundFiles;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class ConnectFourHandler implements the interface Runnable.
 * This one handles all the game logics.
 */
public class ConnectFourHandler implements Runnable {
    private final GameServer.PlayerConnectionHandler player1;
    private final GameServer.PlayerConnectionHandler player2;
    private final ConnectFourBoard board;
    private String move;
    public static volatile boolean isGameEnded;
    private volatile boolean isGameStarted;
    private boolean checkIfGameCanStart = false;

    private List<GameServer.PlayerConnectionHandler> listOfBoardPlayers = new ArrayList<>();

    /**
     *Constructor Method
     * @param player1 - this represents the player 1
     * @param player2 - this represents the player 2
     */
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
/**
 * This method is responsible for starting the game and creating a new game when ever it starts, and ensures that the game remains active as long as there's a started Game.
 * Waits a bit so that players can read the instructions. Broadcasts current player turn. Get player turn input and checks from valid input. Place current player move. Checks for winner. Checks for draw. Broadcasts the board with the new move. Switch move and player.
 */
    @Override
    public void run() {
        GameServer.PlayerConnectionHandler currentPlayer = player1;
        GameServer.PlayerConnectionHandler notPlayingPlayer = player2;
        ReentrantLock lockNotYourTurnInput = new ReentrantLock();


        try {
            animatedStart();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while (!isGameEnded) {
            if (checkIfGameCanStart() && !isGameStarted) {
                startGame();
            }

            if (isGameStarted && !isGameEnded) {

                lockNotYourTurnInput.lock();
                currentPlayer.send(currentPlayer.getName() + Messages.YOUR_TURN);
                notPlayingPlayer.send(notPlayingPlayer.getName() + Messages.MUST_WAIT);
                lockNotYourTurnInput.unlock();


                Integer playerMove = null;
                playerMove = getAnswerAndValidation(currentPlayer, playerMove);

                Sound sound = new Sound();
                board.placePiece(getMove(), playerMove);
                if (currentPlayer == player1) {
                    sound.getSoundClip(SoundFiles.PLAYER1_PIECE.getPath());
                } else {
                    sound.getSoundClip(SoundFiles.PLAYER2_PIECE.getPath());
                }

                if (checkWinnerAndBroadcast(currentPlayer, sound)) break;

                if (checkDrawAndBroadcast()) break;

                broadcast(board.getPrettyBoard());

                switchMove();
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
                notPlayingPlayer = (notPlayingPlayer == player2) ? player1 : player2;
            }
        }
        broadcast(Messages.PLAY_AGAIN_OR_QUIT);
        //TODO FEATURE: Play again or quit
        /*
        while (true) {
            String firstPlayer = null;
            String secondPlayer = null;

            try {
                firstPlayer = currentPlayer.in.readLine();
                secondPlayer = currentPlayer.in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (firstPlayer.equals("/playagain") && secondPlayer.equals("/playagain")) {
                playAgain();
                broadcast("A new game is about to start...");
                startGame();
                break;
            } else {
                break;
            }
        }
         */
    }

    /**
     * This private boolean method is responsible for checking the winner.
     * @param currentPlayer - player whose is playing.
     * @param sound - sound referent to the current player playing. Each player have a different sound when play's.
     * @return - if it's true, current player wins. if false, the players keep playing.
     */
    private boolean checkWinnerAndBroadcast(GameServer.PlayerConnectionHandler currentPlayer, Sound sound) {
        if (board.checkWinner(getMove())) {
            sound.getSoundClip(SoundFiles.GAME_OVER.getPath());
            broadcast(board.getPrettyBoard());
            if (currentPlayer == player1) {
                broadcast(Messages.PLAYER1_WIN);
            } else {
                broadcast(Messages.PLAYER2_WIN);
            }
            return true;
        }
        return false;
    }

    /**
     * This private boolean method is responsible for checking if the game has draw.
     * @return if its true the game ended in a draw. if false the players keep playing.
     */

    private boolean checkDrawAndBroadcast() {
        if (board.checkDraw()) {
            broadcast(board.getPrettyBoard());
            broadcast(Messages.CHECK_DRAW);
            return true;
        }
        return false;
    }
    /**
     * Private method get and validate if the input from the player is correct (must be between 0-6)
     * @param currentPlayer - player who is playing.
     * @param playerMove - players move choice.
     * @return - this return give us the player input choice.
     */

    private Integer getAnswerAndValidation(GameServer.PlayerConnectionHandler currentPlayer, Integer playerMove) {
        while (playerMove == null || board.checkFullColumn(playerMove)) {
            currentPlayer.send(Messages.NUMBER_1TO6);
            String input = null;
            try {
                input = currentPlayer.in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (input.matches("[0-6]")) {
                playerMove = Integer.parseInt(input);

                if (board.checkFullColumn(playerMove)) {
                    currentPlayer.send(Messages.COLUMN_FULL);
                }

            } else {
                currentPlayer.send(Messages.INVALID_INPUT);
            }
        }
        return playerMove;
    }

    /**
     * private void method responsible for broadcast some messages before the game start.
     * @throws InterruptedException if the thread is interrupted.
     */
    private void animatedStart() throws InterruptedException {
        Thread.sleep(1500);
        for (int i = 3; i > 0; i--) {
            broadcast(Messages.GAME_STARTS_IN + i);
            Thread.sleep(1500);
        }
        broadcast(Messages.START_GAME);
        Thread.sleep(3000);
    }

    //TODO FEATURE: Play again
    public void playAgain() {
        board.fillEmptyBoard();
        board.updatePrettyBoard();
        board.resetNumberOfPlays();
        isGameStarted = true;
        isGameEnded = false;
    }

    /**
     * This  public void method is responsible for starting the game and Broadcast the board.
     */

    public void startGame() {
        isGameStarted = true;
        broadcast(board.getPrettyBoard());
    }

    /**
     * This public synchronized void method sends the players inputs.
     */
    public synchronized void broadcast(String message) {
        listOfBoardPlayers.stream()
                .forEach(player -> player.send(message));
    }

    /**
     * This private synchronized boolean method checks if there's enough player for starting the game.
     */
    private synchronized boolean checkIfGameCanStart() {
        return checkIfGameCanStart;
    }

    /**
     * This public synchronized boolean verify if the game can start.
     * @return true the game will start.
     */

    public synchronized boolean theGameCanStart() {
        this.checkIfGameCanStart = true;
        return true;
    }

    /**
     * This public String is responsible for
     * @return
     */
    public String getMove() {
        return move;
    }

    /**
     * This public void is responsible for switch players turn.
     */

    public void switchMove() {
        move = (move == "R") ? "Y" : "R";
    }
}
