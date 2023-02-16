package academy.mindswap.server.commands;

import academy.mindswap.server.GameServer;

public class ListHandler implements CommandHandler {

    @Override
    public void execute(GameServer gameServer, GameServer.playerConnectionHandler playerConnectionHandler) {
       playerConnectionHandler.send(gameServer.listPlayers());
    }
}
