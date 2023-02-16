package academy.mindswap.server.commands;

import academy.mindswap.server.GameServer;

public interface CommandHandler {
    void execute(GameServer gameServer, GameServer.playerConnectionHandler playerConnectionHandler);
}
