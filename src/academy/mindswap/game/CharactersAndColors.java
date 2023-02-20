package academy.mindswap.game;

/**
 * This class contains a number of constant strings representing text colors and styles
 * that are used to customize the appearance of output of our board.
 */

public class CharactersAndColors {
    public static final String RESET = "\033[0m";  // Text Reset
    // Circles
    public static final String CIRCLE_RED = "\033[1;91m⬤\033[0m"; //RED CIRCLE AND RESETS TO BLUE BACKGROUND
    public static final String CIRCLE_YELLOW = "\033[1;93m⬤\033[0m"; //YELLOW CIRCLE AND RESETS TO BLUE BACKGROUND
    public static final String CIRCLE_BLUE = "\033[1;94m⬤\033[0m"; //BLUE CIRCLE AND RESETS TO BLUE BACKGROUND
    public static final String CIRCLE_GREEN = "\033[1;92m⬤\033[0m"; //GREEN CIRCLE AND RESETS TO BLUE BACKGROUND

    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
}

