package academy.mindswap.game;


public class CharactersAndColors {
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    // Circles
    public static final String CIRCLE_RED = "\033[1;91m⬤\033[0m"; //RED CIRCLE AND RESETS TO BLUE BACKGROUND
    public static final String CIRCLE_YELLOW = "\033[1;93m⬤\033[0m"; //YELLOW CIRCLE AND RESETS TO BLUE BACKGROUND
    public static final String CIRCLE_BLUE = "\033[1;94m⬤\033[0m"; //BLUE CIRCLE AND RESETS TO BLUE BACKGROUND
    public static final String CIRCLE_GREEN = "\033[1;92m⬤\033[0m"; //GREEN CIRCLE AND RESETS TO BLUE BACKGROUND


    // Background
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE


    // Winner Background
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE

}

