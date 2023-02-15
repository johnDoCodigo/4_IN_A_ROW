package academy.mindswap.server.commands;

import academy.mindswap.server.Game;
import academy.mindswap.server.messages.Messages;

public class QuitHandler implements CommandHandler {

    @Override
    public void execute(Game game, Game.ClientConnectionHandler clientConnectionHandler) {
        game.removeClient(clientConnectionHandler);
        game.broadcast(clientConnectionHandler.getName(), clientConnectionHandler.getName() + Messages.CLIENT_DISCONNECTED);
        clientConnectionHandler.close();
    }
}
