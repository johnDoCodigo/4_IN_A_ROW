package academy.mindswap.server;

import java.io.IOException;

public class GameLauncher {

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();

        try {
           gameServer.start(8082);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
