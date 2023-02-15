package academy.mindswap.server.commands;

import academy.mindswap.server.Game;

public interface CommandHandler {
    void execute(Game game, Game.ClientConnectionHandler clientConnectionHandler);
}
