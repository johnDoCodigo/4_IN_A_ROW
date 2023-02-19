package academy.mindswap.game;

/**
 * class with the colors of: the pieces, background and the board.
 */

public class CharactersAndColors {
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Circles
    public static final String CIRCLE_RED = "\033[1;91m⬤\033[0m"; //RED CIRCLE AND RESETS TO BLUE BACKGROUND
    public static final String CIRCLE_YELLOW = "\033[1;93m⬤\033[0m"; //YELLOW CIRCLE AND RESETS TO BLUE BACKGROUND
    public static final String CIRCLE_GREEN = "\033[1;92m⬤\033[0m"; //GREEN CIRCLE AND RESETS TO BLUE BACKGROUND

    // Background
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
}

