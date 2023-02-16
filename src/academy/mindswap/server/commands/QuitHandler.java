package academy.mindswap.server.commands;

import academy.mindswap.server.GameServer;
import academy.mindswap.server.messages.Messages;

public class QuitHandler implements CommandHandler {

    @Override
    public void execute(GameServer gameServer, GameServer.playerConnectionHandler playerConnectionHandler) {
        gameServer.removePlayer(playerConnectionHandler);
        gameServer.broadcast(playerConnectionHandler.getName(), playerConnectionHandler.getName() + Messages.PLAYER_DISCONNECTED);
        playerConnectionHandler.close();
    }
}
