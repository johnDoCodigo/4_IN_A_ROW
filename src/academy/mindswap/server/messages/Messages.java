package academy.mindswap.server.messages;

public class Messages {
    public static final String SERVER_STARTED = "Server started on port: %s";
    public static final String DEFAULT_NAME = "PLAYER -";
    public static final String PLAYER_ENTERED_GAME = " entered the game.";
    public static final String NO_SUCH_COMMAND = "⚠️ Invalid command!";
    public static final String COMMANDS_LIST = """
            List of available commands:
            /playagain -> starts a new game
            /list -> gets you the list of connected players
            /quit -> exits the gameServer
            
            
            
            
            /shout <message> -> lets you shout a message to all connected clients
            /whisper <username> <message> -> lets you whisper a message to a single connected player
            /name <new name> -> lets you change your name
            /quit -> exits the gameServer""";
    public static final String PLAYER_DISCONNECTED = " left the chat.";
    public static final String WHISPER_INSTRUCTIONS = "Invalid whisper use. Correct use: '/whisper <username> <message>";
    public static final String NO_SUCH_PLAYER = "The player you want to whisper to doesn't exists.";
    public static final String WHISPER = "(whisper)";
    public static final String WELCOME = "Welcome to MindSwap's 4 in a row Game!";
    public static final String PLAYER_ERROR = "Something went wrong with this player's connection. Error: ";
    public static final String PLAYER_ALREADY_EXISTS = "A player with this name already exists. Please choose another one.";
    public static final String SELF_NAME_CHANGED = "You changed your name to: %s";
    public static final String NAME_CHANGED = "%s changed name to: %s";

    public static final String PLAY_AGAIN = "Do you want to play again?";
    public static final String WAITING_QUEUE = "You have been added to the waiting queue";
}
