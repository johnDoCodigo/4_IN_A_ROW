package academy.mindswap.server.commands;
import academy.mindswap.server.GameServer;
import academy.mindswap.server.messages.Messages;

public class PlayAgainHandler implements CommandHandler {

    @Override
    public void execute(GameServer server, GameServer.PlayerConnectionHandler playerConnectionHandler) {
        String message = playerConnectionHandler.getPlayerInput();
        server.broadcast(playerConnectionHandler.getName(), playerConnectionHandler.getName() + Messages.PLAY_AGAIN);
        //TODO RESET THE GAME BOARD AND NUMBER OF MOVES
        server.getConnectFour().playAgain();
    }
}