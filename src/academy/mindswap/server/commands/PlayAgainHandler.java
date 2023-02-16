package academy.mindswap.server.commands;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

public class PlayAgainHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
        String message = clientConnectionHandler.getMessage();

        server.broadcast(clientConnectionHandler.getName(), clientConnectionHandler.getName() + Messages.PLAY_AGAIN);
        clientConnectionHandler.close();
        }
    }