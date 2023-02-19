package academy.mindswap.server.commands;

import academy.mindswap.server.GameServer;
import academy.mindswap.server.messages.Messages;

public class HelpHandler implements CommandHandler {

    @Override
    public void execute(GameServer gameServer, GameServer.PlayerConnectionHandler playerConnectionHandler) {
        playerConnectionHandler.send(Messages.COMMANDS_LIST);
    }
}
