package academy.mindswap.server.commands;

import academy.mindswap.server.Server;

public class ShoutHandler implements CommandHandler {
    @Override
    public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
        String message = clientConnectionHandler.getMessage();
        String messageToSend = message.substring(6);
        server.broadcast(clientConnectionHandler.getName(), messageToSend.toUpperCase());
    }
}
