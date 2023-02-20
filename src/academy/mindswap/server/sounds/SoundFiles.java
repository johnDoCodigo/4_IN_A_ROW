package academy.mindswap.server.sounds;

public enum SoundFiles {

    GAME_OVER("Resources/GAME_OVER.wav"),
    PLAYER1_PIECE("Resources/PLAYER1_PIECE.wav"),
    PLAYER2_PIECE("Resources/PLAYER2_PIECE.wav");
    private final String path;

    SoundFiles(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}
