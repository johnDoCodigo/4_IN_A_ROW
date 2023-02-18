package academy.mindswap.server.commands;

import academy.mindswap.server.GameServer;
import academy.mindswap.server.messages.Messages;

import java.util.Optional;

public class WhisperHandler implements CommandHandler {

    @Override
    public void execute(GameServer gameServer, GameServer.PlayerConnectionHandler playerConnectionHandler) {
        String message = playerConnectionHandler.getPlayerChoiceInput();

        if (message.split(" ").length < 3) {
            playerConnectionHandler.send(Messages.WHISPER_INSTRUCTIONS);
            return;
        }

        Optional<GameServer.PlayerConnectionHandler> receiverClient = gameServer.getClientByName(message.split(" ")[1]);

        if (receiverClient.isEmpty()) {
            playerConnectionHandler.send(Messages.NO_SUCH_PLAYER);
            return;
        }

        String messageToSend = message.substring(message.indexOf(" ") + 1).substring(message.indexOf(" ") + 1);
        receiverClient.get().send(playerConnectionHandler.getName() + Messages.WHISPER + ": " + messageToSend);
    }
}
