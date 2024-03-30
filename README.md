Draughts game - IN PROGRESS. Watch this space! :)
Assumptions -

The game could ultimately be extended to accommodate thousands of players. There therefore needs to be a way of ensuring 
that clients only receive the messages that are meant for them, and that messages are deleted when no longer needed.

Also, we need to accommodate the fact that some messages are meant for both players.

It is assumed that there is a 1:1 mapping between Players and client Ids.

SEQUENCE OF EVENTS
==================

## Prior to requesting a game
Two Client objects are created. For each Client, the following takes place:

- The Client object gives itself an uuid as an id.

- The Client creates a ClientMessagingService object as an instance variable, passing in its id to the 
ClientMessagingService constructor and invoking the ClientMessagingService establishConnection() method. The 
establishConnection() sends a message to the server to establish a connection with the server. When a connection is 
created, the server puts the Session object in a hashmap with the client id as the key.

- The Client object also creates a ClientController object, and passes itself to the ClientController constructor. The 
ClientController has its own instance of a DraughtBoardView object, which is the class responsible for the view.

- The Client calls the ClientController's setUp() method, which fires up a GUI, using its instance of the DraughtBoardView 
class. It does so by invoking the view's setUp method.

- The GUI gives the user an option to request a game. When the user selects this option, a request is sent to the server.

## New game requests

The server adds the client id to a linked list of client ids relating to clients awaiting games. When the list is at least 
two items in length, a Game object is created for those clients and the relevant IDs are popped from the linked list.

The Player objects for the Game object are obtained using the clientIdToPlayer maps. The Game object has a UUID. The 
server now does the following:

- Adds the Game object and the relevant clientIds to a clientIdToGame hashmap.
- Tells the clients that a game has been arranged and tells each client which colour they are (red plays first - the 
first client id to be retrieved from the linked list is red.

## Game in progress

The game is now in progress, and players can now do the following:

- Make a piece
- Offer a draw.
- Accept a draw (if it was made on the same move* as when the draw offer was made).
- Resign

* A move is defined as the movement of a piece, which may or may not terminate with a change of turn.

After each move, the server monitors the following:

- Whether a game is a draw (whether due to a stalemate or a draw acceptance).
- Whether the game is won (whether due to a resignation or a winning position).

If also checks whether moves submitted are legal and made by the player whose turn it is.

If there is a draw offer pending, it is cancelled when a new move is made. 

The server updates one or both clients in response to events, as required. 

If a player exits their GUI, the other player is informed, and the game is abandoned. 