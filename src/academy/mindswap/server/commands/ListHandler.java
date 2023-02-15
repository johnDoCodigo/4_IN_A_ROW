package academy.mindswap.server.commands;

import academy.mindswap.server.Game;

public class ListHandler implements CommandHandler {

    @Override
    public void execute(Game game, Game.ClientConnectionHandler clientConnectionHandler) {
       clientConnectionHandler.send(game.listClients());
    }
}
