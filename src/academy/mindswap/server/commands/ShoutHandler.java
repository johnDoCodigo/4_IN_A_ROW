package academy.mindswap.server.commands;

import academy.mindswap.server.GameServer;

public class ShoutHandler implements CommandHandler {
    @Override
    public void execute(GameServer gameServer, GameServer.PlayerConnectionHandler playerConnectionHandler) {
        String message = playerConnectionHandler.getPlayerInput();
        String messageToSend = message.substring(6);
        gameServer.broadcast(playerConnectionHandler.getName(), messageToSend.toUpperCase());
    }
}
