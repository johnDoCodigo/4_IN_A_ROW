package academy.mindswap.server;

import java.io.IOException;

public class ServerLauncher {

    public static void main(String[] args) {
        Server server = new Server();

        try {
           server.start(8082);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
