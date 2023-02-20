
package academy.mindswap.server;
import java.io.IOException;

/**
 * Class responsible for creating the gameServer
 */

public class GameLauncher {

    /**
     * Main method of the class GameLauncher
     * Accepts a port as an argument. If no port is provided the default is 8083
     *
     * @param args the port to start the GameLauncher
     */

    public static void main(String[] args) {

        GameServer gameServer = new GameServer();

        try {
           gameServer.start(8083);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
