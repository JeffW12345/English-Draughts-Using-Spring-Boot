OVERVIEW
========

This app allows two clients on the same host allow two users to play English draughts against each other, with a server
also running locally. It is a revised and improved version of a project I did at university - 
https://github.com/JeffW12345/EnglishDraughts. 

The app uses the Model View Controller paradigm, and uses Spring Boot's WebSocket library to achieve push-based
communications between the clients and the server.

RUNNING INSTRUCTIONS
====================

To run the application, follow these steps:

- Clone the repository to your local machine.
- Navigate to the project directory.
- Ensure you have Maven installed.
- Run the mvn spring-boot:run command to start the application.
- Open two separate instances of the application to simulate two players.
- Follow the on-screen instructions to play the game.

ASSUMPTIONS
===========

The game follows the rules of English draughts - https://en.wikipedia.org/wiki/English_draughts.

There is just one player per Client instance.

If either player closes their GUI, this action results in the app terminating.

LIMITATIONS
===========

This is an MVP version, and won't cover:

- More than 2 players. However, I will make it so that my server to be open to being extended to accommodate > 2 players.
- Logins
- The clients and server running on different devices.
- Different board positioning, depending on whether the player is red or white.
- Saving games or results to a database.
- Players timing out due to inactivity

SEQUENCE OF EVENTS
==================

## Prior to requesting a game

Two Client objects are created. For each Client, the following takes place:

- The Client object gives itself a uuid as an id.

- The Client creates a ClientMessagingService object as an instance variable, passing in its id to the
  ClientMessagingService constructor and invoking the ClientMessagingService establishConnection() method. The
  establishConnection() sends a message to the server to establish a connection with the server. When a connection is
  created, the server puts the Session object in a hashmap with the client id as the key (via a static method in the
  ClientIdToSessionMapping class).

- The Client object creates a MasterClientController object, and passes itself to the ClientController constructor. The
  MasterClientController constructor creates its own instance of a DraughtsBoardGui object, which is the class 
  responsible for the view. The MasterClientController constructor also creates its own instances of helper controller 
  classes.

- The Client calls the newly created MasterClientController object's setUp() method, which fires up a GUI, using its instance
  of the DraughtBoardView class. It does so by invoking its DraughtsBoardGui object's setUp method.

## New game requests

The GUI gives the user an option to request a game. When the user selects this option, a request is sent to the server.

The server then consults a static linked list of client ids relating to clients awaiting games (stored in the
ClientsAwaitingAGame class). If there is at least one client id in the list, then that client id is popped and a game is 
set up between the current client and the client relating to that client id. This entails the following actions:

- Greating a Game object.
- Creating a Player object for each client. One Player is chosen to be the white player and the other is chosen to 
  represent the red player. The Player objects create UUID ids when they are instantiated, and they are passed their 
  client's id and their colour in their constructor.
- The Player objects are assigned to the Game objects redPlayer and whitePlayer attributes, depending on which colour 
  they were assigned.
- The Player ids are mapped to the Game object using the PlayerIdToGameMapping class.
- The server the clients that a game has been arranged and tells each client which colour they are playing.

If there are no client ids in the ClientsAwaitingAGame linked list, the client id of the current user is added to that 
list, and they will join a game when their client id is popped by another user.

## Game in progress

When the game is progress, and players can now do the following, via the GUI:

- Make a move*. The player does this by clicking on two squares, which represent the start and end squares respectively. 
  The move is sent to the server by the client. The server checks whether the move is legal. If it is, both clients are 
  given an updated board view (and told that it is now the other player's turn, if applicable). If the move is not 
  legal, the client making the move is informed.

- Offer a draw. If a player offers a draw, the server is informed, which informs the other player. The offer is 
  cancelled once the next move has been made, and the GUIs are updated accordingly.

- Accept a draw. If a draw is accepted, the server is informed, and it informs the other client. The game is then over.

- Resign. If a player resigns, the server is informed, and it informs the other client. The game is then over.

* A move is defined as the movement of a piece, which may or may not terminate with a change of turn.

The server checks if moves are legal and promotes men to kings where required.

After each move, the server monitors the following:

- Whether a game is a draw due to stalemate
- Whether the game is won

If also checks whether moves submitted are legal and made by the player whose turn it is.

If there is a draw offer pending, it is cancelled when a new move is made.

The server updates one or both clients in response to events, as required.

If a player exits their GUI, the other player is informed, and the game is abandoned. 