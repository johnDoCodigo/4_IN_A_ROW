package academy.mindswap.server.commands;

import academy.mindswap.server.Server;

public class ListHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
       clientConnectionHandler.send(server.listClients());
    }
}
