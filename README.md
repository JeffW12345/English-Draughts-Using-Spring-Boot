OVERVIEW
========

This app allows two clients on the same host allow two users to play English draughts against each other, with a server
also running locally. It is a revised and improved version of a project I did at university - 
https://github.com/JeffW12345/EnglishDraughts. 

The server runs a Spring Boot server. The client instances send messages to that server using the Jakarta WebSocket
library. The application code does not actively listen for messages to the clients - instead, they are received on a
push basis, with the listening being handled by the library code.

The Jakarta WebSocket library is used for client-server communication. With WebSocket, when the client sends the server 
a message, the server gets passed a Session object specific to that client via the method annotated with @OnOpen. If the 
server wishes to send a message to that client, it can use 'session.getBasicRemote().sendText(message)' to send a 
message to that client. Similarly, the client has a Session object relating to the server, it can 

The server is able to send messages to either one of the Client objects relating to a game, or to both of them, in 
response to a message from either client. This is possible because:

- Server-side mappings are used such that, if the id of one of the Client objects is known, the id of the other Client 
object can be determined. 
- Mapping is also in place to make it so that, if the server knows a client's id, the relevant Session object used for 
communication between that client and the server can be obtained.



RUNNING INSTRUCTIONS
====================

To run the application, follow these steps:

- Clone the repository to your local machine.
- Navigate to the project directory.
- Ensure you have Maven installed.
- Run the mvn spring-boot:run command to start the application.
- Open two separate instances of the application to simulate two players.
- Follow the on-screen instructions to play the game.

This application was built using Java 17, though it may work with earlier versions of Java. 

ASSUMPTIONS
===========

The game follows the rules of English draughts - https://en.wikipedia.org/wiki/English_draughts.

There is just one player per Client instance.

If a player closes their GUI, the other player is informed, and the game is abandoned and the app closes. 

A win is determined in accordance to the aforementioned Wikipedia page: 'A player wins by capturing all of the 
opponent's pieces or by leaving the opponent with no legal move'.

Draws only occur by mutual consent. 

A move is defined as the movement of a piece, which may or may not terminate with a change of turn.

LIMITATIONS
===========

This is an MVP version, and won't cover:

- More than 2 players. However, I will make it so that my server to be open to being extended to accommodate > 2 players.
- Logins.
- The clients and server running on different devices.
- Different board positioning, depending on whether the player is red or white.
- Saving games or results to a database.
- Players timing out due to inactivity.
- Storing past games or results in a database.
- Requesting a new game once the current game has concluded. 

These features may be added in a future update, however. 

SEQUENCE OF EVENTS
==================

## Prior to requesting a game

The DraughtsApplication class creates a Spring Boot server instance, and then creates two Client objects, each on a 
separate thread. For each Client object, the following takes place:

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

- The Client calls the newly created MasterClientController object's setUp() method, which fires up a GUI, using its 
instance of the DraughtBoardView class. It does so by invoking its DraughtsBoardGui object's setUp method.

## New game requests

The GUI gives the user an option to request a game. When the user selects this option, a request is sent to the server.

The server then consults a static linked list of client ids relating to clients awaiting games (stored in the
ClientsAwaitingAGame class). If there is at least one client id in the list, then that client id is popped and a game is 
set up between the current client and the client relating to that client id. This entails the following actions:

- Creating a Game object.
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

**Make a move**. The player does this by clicking on two squares, which represent the start and end squares 
respectively. 

The move is sent to the server by the client in a JSON encoded ClientMessagingService object. The ClientMessagingService
object consists of the following:

- The clientId String.
- A Move object, representing the move being submitted.
- The colour of the pieces used by the client's player. 
- The request type (a move request).

When the server receives the message, it does the following:

- It begins by retrieving the relevant Game object using a static method in the ClientIdToGameMapping class. It then 
adds the Move object to the Game object's list of moves for the relevant colour. 

- It then checks whether the requested move is legal. 

If the move is legal, the server does the following:

- Updates the MoveStatus attribute of the Move object to COMPLETE. 
- Updates the Board object. 
- Checks whether the current player has another turn (because they have just jumped over a piece and another jump is 
possible using the same piece that did the last jump). If another move is possible, the clients are informed of this and
given the new board layout. 
- If the turn is over, the client checks whether the game is now won by the current player. 
- If the player who has just moved has won, both players' boards are updated and the clients are informed. 
- If there is no win, the clients are told that the current move is over and given an updated board.

The GUI messages and boards are updated in response to messages from the server. 

**Offer a draw**. If a player offers a draw, the server is informed, which informs the other player. The offer is 
cancelled once the next move has been made, and the GUIs are updated accordingly.

**Accept a draw** If a draw is accepted, the server is informed, and it informs the other client. The game is then over.

**Resign** If a player resigns, the server is informed, and it informs the other client. The game is then over.

The server checks if moves are legal and promotes men to kings where required.

COMMUNICATIONS PROTOCOL
=======================

## A. Client sending messages

When a client needs to send a message to a client, it is first created as an object of type ClientMessageToServer. This
class contains an attribute of type ClientMessageToServer. This class is an enum class with the following attributes, 
which describe the type of request being made: 

WANT_GAME, MOVE_REQUEST, DRAW_OFFER, DRAW_ACCEPT, RESIGN, EXITING_DUE_TO_GUI_CLOSE, ESTABLISH_SESSION

The ClientMessageToServer also contains other instance variables, such as a Move object, which will be null if they are
not relevant to the particular message being sent. 

A ClientMessageToServer object is created for the following types of requests: 

**Establishing an initial connection with the server** In order for a message exchanges to take place, the client needs 
to set up a session with the server. It does so using the establishSession() method in ClientMessageDispatchService.
The ClientMessageToServer object contains the Session object used to make the connection request, as well as a 
String attribute containing the Client id as a String and a ClientMessageToServer constant of 'ESTABLISH_SESSION'.
When the the Server receives the message, it stores the Session as a value in a hashmap, with the client id String as 
the key.

**Game requests**. The object contains String attribute containing the Client id as a String and a ClientMessageToServer 
constant of 'WANT_GAME'. 

**Move requests** The object incorporates the Client id (as a String), a Move object, a Colour object (representing the
player's colour) and a ClientMessageToServer constant of MOVE_REQUEST.

**Draw offers** The object contains String attribute containing the Client id as a String and a ClientMessageToServer
constant of 'DRAW_OFFER'.

**Draw acceptance** The object contains String attribute containing the Client id as a String and a ClientMessageToServer
constant of 'DRAW_ACCEPT'.

**Resignation** The object contains String attribute containing the Client id relating to the resigning player as a 
String and a ClientMessageToServer constant of 'RESIGN'.

**Exiting the game** When a client closes their GUI, this results in a message being sent from that client to the server,
which sends a message to the other client to get it to shut down. In this instance, the client with the window being 
closed sends a message to the server, using a ClientMessageToServer object with a ClientMessageToServer constant of 
'EXITING_DUE_TO_GUI_CLOSE', and the client id as a String. 

Messages are converted to JSON, and then sent to the server using the ClientMessageDispatchService class. 

## B. Server sending messages

When the server sends a message to a client, it creates a ServerMessageToClient object (which is later rendered into
JSON). The ServerMessageToClient object contains a ServerToClientMessageType attribute, which is an enum representing
the message type, that tells the client the purpose of the message. These are the enum constants: 

    ASSIGN_RED_COLOUR
    ASSIGN_WHITE_COLOUR
    UPDATE_BOARD_SAME_TURN
    UPDATE_BOARD_CHANGE_OF_TURN
    DECLINE_MOVE
    INFORM_RED_IS_WINNER
    INFORM_WHITE_IS_WINNER
    INFORM_OF_DRAW_ACCEPTED
    INFORM_OTHER_PLAYER_RESIGNED
    INFORM_DRAW_OFFER_MADE
    INFORM_OTHER_CLIENT_CLOSED_WINDOW

The message is encoded in JSON format before being sent to the client. 

The ServerMessageComposeService class contains methods that produce messages for one or both clients taking part in a 
(as applicable), covering the scenarios described by the enums above. The client knows that the game is starting when 
it receives a notification containing 'ASSIGN_RED_COLOUR' or 'ASSIGN_WHITE_COLOUR'. 

Some of the communications contain a Board object, to enable the client GUIs to be updated. 




