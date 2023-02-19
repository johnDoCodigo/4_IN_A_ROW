Connect 4

Rules:
Connect four is a very simple gameServer. It is always played between 2 players and on a 7x6 space board. 
Each turn, each player chooses a column to place his piece, it drops down to the first available square. 
The one who manages to place 4 consecutive pieces in a row horizontally, vertical or diagonal wins. 
If no one succeeds, the match ends in a draw.


History

This gameServer is centuries old, Captain James Cook used to play it with his fellow officers on his long voyages, 
and so it has also been called "Captain's Mistress". 
Milton Bradley (now owned by Hasbro) published a version of this gameServer called "Connect Four" in 1974.

Other names for this gameServer are "Four-in-a-Row" and "Plot Four".




Core Concepts:
1. 2D Arrays - The Connect four board is represented by a 2D array containing the pieces.
2. Socket - pipe for communication between the players and server.
3. Buffered Reader and Buffered Writer - write and transmits the messages between "players1 - server  - players2".
4. Enum - used for representing the commands.
5. Interface - used for specified the behaviour for the commands.
6. Optional Collector -


Implementation:
Class GameLauncher - initiates the server, connecting it to the port 8082;
Class Server - establish the connection between the players. between the players and the server.

Package Game:
Class ConnectFour - here contains most of the game's logic.
Class Characters and Colors - responsible for the color surrounding our backboard, the circles, winner Background.

Package Player:
Class Player - responsible for creating the players. Have a socket, buffered writer and reader.

Package SERVER: Contains 3 packages:

Package Commands:
Has different classes for all the commands you can use on the game;
has an Interface called "CommandHandler" responsible the interaction between all commands;
Enum called Command, with the description of the commands available.

Package Sounds:
Sounds for the Game Winner.

Package messages:
Class Messages - contains all the messages that are available in the game.