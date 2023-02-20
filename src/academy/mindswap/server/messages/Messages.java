
package academy.mindswap.server.messages;
import academy.mindswap.game.CharactersAndColors;

/**
 * Messages sent to player by connect four game
 */

public class Messages {
    public static final String SERVER_STARTED = "Server started on port: %s";
    public static final String DEFAULT_NAME = "PLAYER -";
    public static final String PLAYER_ENTERED_GAME = " entered the game.";
    public static final String WAITING_FOR_OTHER_PLAYERS = "Waiting for other players to join the game...";
    public static final String NO_SUCH_COMMAND = "⚠️ Invalid command!";
    public static final String ASK_NAME = "What's your name?";
    public static final String INSTRUCTIONS =
            "----------------------------------INSTRUCTIONS----------------------------------\n" +
            "Each round a player must choose a column from 0-6.\n"+
            "The first player to connect four "+CharactersAndColors.CIRCLE_GREEN+ " in a row, horizontally, vertically or diagonally, wins the game!\n"+
            "Red goes first " + CharactersAndColors.CIRCLE_RED+".\n"+
            "Yellow goes next " + CharactersAndColors.CIRCLE_YELLOW+".\n"+
            "Good luck!\n"+
            "--------------------------------------------------------------------------------\n";

    public static final String COMMANDS_LIST = """
            List of available commands:
            /playagain -> starts a new game
            /list -> gets you the list of connected players
            /name <new name> -> lets you change your name
            /quit -> exits the gameServer""";

    public static final String PLAYER_DISCONNECTED = " left the chat.";
    public static final String WELCOME = "Welcome to MindSwap's 'Connect Four' Game!";
    public static final String PLAY_AGAIN_OR_QUIT = "Type /playagain if you would like to play again.\n"+
            "Type /quit to leave.";

    //TODO FEATURE
    public static final String WAITING_QUEUE = "You have been added to the waiting queue";
    public static final String CHECK_DRAW =
            "██╗████████╗███████╗     █████╗     ██████╗ ██████╗  █████╗ ██╗    ██╗██╗\n" +
            "██║╚══██╔══╝██╔════╝    ██╔══██╗    ██╔══██╗██╔══██╗██╔══██╗██║    ██║██║\n" +
            "██║   ██║   ███████╗    ███████║    ██║  ██║██████╔╝███████║██║ █╗ ██║██║\n" +
            "██║   ██║   ╚════██║    ██╔══██║    ██║  ██║██╔══██╗██╔══██║██║███╗██║╚═╝\n" +
            "██║   ██║   ███████║    ██║  ██║    ██████╔╝██║  ██║██║  ██║╚███╔███╔╝██╗\n" +
            "╚═╝   ╚═╝   ╚══════╝    ╚═╝  ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚══╝╚══╝ ╚═╝\n" +
            "Game over, if you want do play again please type /playagain.";

    public static final String COLUMN_FULL = "This column is full. Choose another one.";

    public static final String PLAYER1_WIN =
            "██████╗ ██╗      █████╗ ██╗   ██╗███████╗██████╗      ██╗    ██╗    ██╗██╗███╗   ██╗███████╗██╗\n" +
            "██╔══██╗██║     ██╔══██╗╚██╗ ██╔╝██╔════╝██╔══██╗    ███║    ██║    ██║██║████╗  ██║██╔════╝██║\n" +
            "██████╔╝██║     ███████║ ╚████╔╝ █████╗  ██████╔╝    ╚██║    ██║ █╗ ██║██║██╔██╗ ██║███████╗██║\n" +
            "██╔═══╝ ██║     ██╔══██║  ╚██╔╝  ██╔══╝  ██╔══██╗     ██║    ██║███╗██║██║██║╚██╗██║╚════██║╚═╝\n" +
            "██║     ███████╗██║  ██║   ██║   ███████╗██║  ██║     ██║    ╚███╔███╔╝██║██║ ╚████║███████║██╗\n" +
            "╚═╝     ╚══════╝╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝     ╚═╝     ╚══╝╚══╝ ╚═╝╚═╝  ╚═══╝╚══════╝╚═╝\n" +
            "                                                                                               ";
    public static final String PLAYER2_WIN =
            "██████╗ ██╗      █████╗ ██╗   ██╗███████╗██████╗     ██████╗     ██╗    ██╗██╗███╗   ██╗███████╗██╗\n" +
            "██╔══██╗██║     ██╔══██╗╚██╗ ██╔╝██╔════╝██╔══██╗    ╚════██╗    ██║    ██║██║████╗  ██║██╔════╝██║\n" +
            "██████╔╝██║     ███████║ ╚████╔╝ █████╗  ██████╔╝     █████╔╝    ██║ █╗ ██║██║██╔██╗ ██║███████╗██║\n" +
            "██╔═══╝ ██║     ██╔══██║  ╚██╔╝  ██╔══╝  ██╔══██╗    ██╔═══╝     ██║███╗██║██║██║╚██╗██║╚════██║╚═╝\n" +
            "██║     ███████╗██║  ██║   ██║   ███████╗██║  ██║    ███████╗    ╚███╔███╔╝██║██║ ╚████║███████║██╗\n" +
            "╚═╝     ╚══════╝╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝    ╚══════╝     ╚══╝╚══╝ ╚═╝╚═╝  ╚═══╝╚══════╝╚═╝\n" +
            "                                                                                                   ";
    public static final String START_GAME = "\n"+ "-".repeat(20) + "LOADING" + "-".repeat(20) + "\n";

    public static final String SERVER_ERROR = "Something went wrong with the server. Connection closing...";
    public static final String YOUR_TURN = ", it's your turn!";
    public static final String MUST_WAIT = ", you must wait for your turn.";
    public static final String NUMBER_1TO6 = "Enter a number between 0 and 6:";
    public static final String INVALID_INPUT = "Invalid input.";
    public static final String GAME_STARTS_IN = "GAME STARTS IN: ";
}
