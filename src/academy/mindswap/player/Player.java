package academy.mindswap.player;

import java.io.*;
import java.net.Socket;

public class Player {

    /**
     * Main method of the class Player.
     * Accepts a server address and a port as an argument.
     * If no address is provided the default is localhost.
     * @param args the address and the port to start the Player.
     */
    public static void main(String[] args) {
        Player player = new Player();
        try {
            player.start("localhost", 8083);
        } catch (IOException e) {
            System.out.println("Connection closed...");
        }
    }

    /**
     * Starts the player in specified port.
     * Create a new thread to send messages to game.
     * @throws IOException when it's not possible to connect to the server.
     */
    private void start(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        new Thread(new KeyboardHandler(out, socket)).start();
        String line;
        while (( line = in.readLine()) != null) {
            System.out.println(line);
        }
        socket.close();
    }

    /**
     * Class to handle input from the keyboard and send it over a network socket connection.
     */
    private class KeyboardHandler implements Runnable {
        private BufferedWriter out;
        private Socket socket;
        private BufferedReader in;

        /**
         * Constructor method to initialize the properties.
         */
        public KeyboardHandler(BufferedWriter out, Socket socket) {
            this.out = out;
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(System.in));
        }


        /**
         * Method that runs until the socket is closed.
         * Reads a line of text from the keyboard input using the "readLine" method of the "BufferedReader" object.
         * Then writes the line and flushes the output stream.
         * If the input line is "/quit", the socket is closed and the program exits.
         * If there is an exception while reading,the socket is closed and an error message is printed to the console.
         */
        @Override
        public void run() {
            while (!socket.isClosed()) {
                try {
                    String line = in.readLine();

                    out.write(line);
                    out.newLine();
                    out.flush();

                    if (line.equals("/quit")) {
                        socket.close();
                        System.exit(0);
                    }
                } catch (IOException e) {
                    System.out.println("Something went wrong with the server. Connection closing...");
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}