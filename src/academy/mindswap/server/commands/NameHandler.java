package academy.mindswap.server.commands;

import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;


public class NameHandler implements CommandHandler{
    @Override
    public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
        String message = clientConnectionHandler.getMessage();
        String name = message.substring(7);
        String oldName = clientConnectionHandler.getName();
        server.getClientByName(name).ifPresentOrElse(
                client -> clientConnectionHandler.send(Messages.CLIENT_ALREADY_EXISTS),
                () -> {
                    clientConnectionHandler.setName(name);
                    clientConnectionHandler.send(Messages.SELF_NAME_CHANGED.formatted(name));
                    server.broadcast(name, Messages.NAME_CHANGED.formatted(oldName, name));
                }
        );
    }
}
