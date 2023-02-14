package academy.mindswap.server.commands;

import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

public class QuitHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
        server.removeClient(clientConnectionHandler);
        server.broadcast(clientConnectionHandler.getName(), clientConnectionHandler.getName() + Messages.CLIENT_DISCONNECTED);
        clientConnectionHandler.close();
    }
}
