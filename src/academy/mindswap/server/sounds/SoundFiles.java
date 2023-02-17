package academy.mindswap.server.sounds;

public enum SoundFiles {

    GAME_OVER("/home/filipe/IdeaProjects/4_IN_A_ROW/sounds/GAME_OVER.wav"),
    PLAYER1_PIECE("/home/filipe/IdeaProjects/4_IN_A_ROW/sounds/PLAYER1_PIECE.wav"),
    PLAYER2_PIECE("/home/filipe/IdeaProjects/4_IN_A_ROW/sounds/PLAYER2_PIECE.wav");
    private final String path;

    SoundFiles(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}
