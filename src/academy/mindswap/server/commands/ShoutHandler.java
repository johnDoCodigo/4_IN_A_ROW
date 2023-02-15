package academy.mindswap.server.commands;

import academy.mindswap.server.Game;

public class ShoutHandler implements CommandHandler {
    @Override
    public void execute(Game game, Game.ClientConnectionHandler clientConnectionHandler) {
        String message = clientConnectionHandler.getMessage();
        String messageToSend = message.substring(6);
        game.broadcast(clientConnectionHandler.getName(), messageToSend.toUpperCase());
    }
}
