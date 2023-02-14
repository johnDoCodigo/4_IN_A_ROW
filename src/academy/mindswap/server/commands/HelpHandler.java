package academy.mindswap.server.commands;

import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

public class HelpHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
        clientConnectionHandler.send(Messages.COMMANDS_LIST);
    }
}
