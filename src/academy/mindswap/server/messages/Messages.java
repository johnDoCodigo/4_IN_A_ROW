package academy.mindswap.server.messages;

import academy.mindswap.game.CharactersAndColors;

public class Messages {
    public static final String SERVER_STARTED = "Server started on port: %s";
    public static final String DEFAULT_NAME = "PLAYER -";
    public static final String PLAYER_TURN = "%s is playing now\n";
    public static final String PLAYER_ENTERED_GAME = " entered the game.";
    public static final String PLAYER_JOINED = "Player %s joined the game!\n";
    public static final String PLAYER_LEFT_GAME = "%s has left the game\n";
    public static final String WAITING_FOR_OTHER_PLAYERS = "Waiting for other players to join the game...";
    public static final String NO_SUCH_COMMAND = "⚠️ Invalid command!";
    public static final String ASK_NAME = "What's your name?";
    public static final String INSTRUCTIONS =
            "----------------------------------INSTRUCTIONS----------------------------------\n" +
            "The game is very simple, you must choose a column from 0-6.\n"+
            "The first player to connect four "+CharactersAndColors.CIRCLE_GREEN+ " in a row, horizontally, vertically or diagonally, wins the game!\n"+
            "Red goes first " + CharactersAndColors.CIRCLE_RED+".\n"+
            "Yellow goes next " + CharactersAndColors.CIRCLE_YELLOW+".\n"+
            "Best of luck!\n"+
            "--------------------------------------------------------------------------------\n";

    public static final String COMMANDS_LIST = """
            List of available commands:
            /playagain -> starts a new game
            /list -> gets you the list of connected players
            /quit -> exits the gameServer
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
    public static final String PLAY_AGAIN_OR_QUIT = "Type /playagain if you would like to play again.\n"+
            "Type /quit to leave.";
    public static final String WAITING_QUEUE = "You have been added to the waiting queue";
    public static final String CHECK_DRAW =
            "██╗████████╗███████╗     █████╗     ██████╗ ██████╗  █████╗ ██╗    ██╗██╗\n" +
            "██║╚══██╔══╝██╔════╝    ██╔══██╗    ██╔══██╗██╔══██╗██╔══██╗██║    ██║██║\n" +
            "██║   ██║   ███████╗    ███████║    ██║  ██║██████╔╝███████║██║ █╗ ██║██║\n" +
            "██║   ██║   ╚════██║    ██╔══██║    ██║  ██║██╔══██╗██╔══██║██║███╗██║╚═╝\n" +
            "██║   ██║   ███████║    ██║  ██║    ██████╔╝██║  ██║██║  ██║╚███╔███╔╝██╗\n" +
            "╚═╝   ╚═╝   ╚══════╝    ╚═╝  ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚══╝╚══╝ ╚═╝\n" +
            "Game over, if you want do play again please write /playagain.";

    public static final String INVALID_COLUMN = "You must choose a column between 0-6.";
    public static final String WAIT_TURN = "You must wait for your turn.";

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

    public static final String NEW_GAME = "A NEW GAME WAS CREATED";
    public static final String START_GAME = "\n--The connect four game will now start!--\n";


}
