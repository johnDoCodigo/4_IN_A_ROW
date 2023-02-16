package academy.mindswap.server.commands;

import academy.mindswap.server.GameServer;
import academy.mindswap.server.messages.Messages;


public class NameHandler implements CommandHandler{
    @Override
    public void execute(GameServer gameServer, GameServer.playerConnectionHandler playerConnectionHandler) {
        String message = playerConnectionHandler.getMessage();
        String name = message.substring(6);
        String oldName = playerConnectionHandler.getName();
        gameServer.getClientByName(name).ifPresentOrElse(
                client -> playerConnectionHandler.send(Messages.PLAYER_ALREADY_EXISTS),
                () -> {
                    playerConnectionHandler.setName(name);
                    playerConnectionHandler.send(Messages.SELF_NAME_CHANGED.formatted(name));
                    gameServer.broadcast(name, Messages.NAME_CHANGED.formatted(oldName, name));
                }
        );
    }
}
