package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.Player;
import com.github.jeffw12345.draughts.server.ClientsAwaitingAGame;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToGameMapping;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToSessionMapping;
import com.github.jeffw12345.draughts.models.messaging.ClientMessageToServer;
import com.github.jeffw12345.draughts.models.messaging.message.ClientToServerRequestType;
import com.github.jeffw12345.draughts.server.mapping.PlayerIdToGameMapping;

public class MessageController {
    public static void processMessageFromClient(ClientMessageToServer clientRequestToServer) {
        ClientToServerRequestType clientToServerRequestType= clientRequestToServer.getRequestType();
        switch (clientToServerRequestType) {
            case WANT_GAME:
                wantGameActions(clientRequestToServer);
                break;
            case MOVE_REQUEST:
                moveRequestActions(clientRequestToServer);
                break;
            case DRAW_OFFER:
                drawOfferActions(clientRequestToServer);
                break;
            case DRAW_ACCEPT:
                drawAcceptActions(clientRequestToServer);
                break;
            case RESIGN:
                resignationActions(clientRequestToServer);
                break;
            case ACCEPT_GAME:
                acceptGameActions(clientRequestToServer);
                break;
            case EXIT:
                exitActions(clientRequestToServer);
                break;
            case CHECK_FOR_UPDATE:
                checkForUpdateActions(clientRequestToServer);
                break;
            case ESTABLISH_SESSION:
                establishConnectionActions(clientRequestToServer);
                break;
            default:
                throw new IllegalArgumentException("Unexpected response type: " + clientToServerRequestType);
        }
    }

    private static void establishConnectionActions(ClientMessageToServer clientRequestToServer) {
        ClientIdToSessionMapping.add(clientRequestToServer.getClientId(),
                clientRequestToServer.getClient().getClientMessagingService().getSession());
    }

    private static void checkForUpdateActions(ClientMessageToServer clientRequestToServer) {
    }
    private static void exitActions(ClientMessageToServer clientRequestToServer) {
    }

    private static void acceptGameActions(ClientMessageToServer clientRequestToServer) {
    }

    private static void resignationActions(ClientMessageToServer clientRequestToServer) {
    }

    private static void drawAcceptActions(ClientMessageToServer clientRequestToServer) {
    }

    private static void drawOfferActions(ClientMessageToServer clientRequestToServer) {
    }

    private static void moveRequestActions(ClientMessageToServer clientRequestToServer) {
    }

    private static void wantGameActions(ClientMessageToServer clientRequestToServer) {
        String clientId = clientRequestToServer.getClient().getClientId();
        if (ClientsAwaitingAGame.atLeastOnePlayerSeekingGame()){
            String otherClientId = ClientsAwaitingAGame.pop();
            newGameSetup(clientId, otherClientId);
        }
        else{
            ClientsAwaitingAGame.addClientId(clientId);
        }
    }

    private static void newGameSetup(String redPlayerClientId, String whitePlayerClientId) {
        Game game = new Game();

        Player redPlayer = new Player(redPlayerClientId, Colour.RED);
        game.addPlayer(redPlayer);

        Player whitePlayer = new Player(whitePlayerClientId, Colour.WHITE);
        game.addPlayer(whitePlayer);

        PlayerIdToGameMapping.assignPlayerToGame(redPlayer, game);
        PlayerIdToGameMapping.assignPlayerToGame(whitePlayer, game);

        ClientIdToGameMapping.assignClientIdToGame(redPlayerClientId, game);
        ClientIdToGameMapping.assignClientIdToGame(whitePlayerClientId, game);

        redPlayer.newGameClientNotifications(game);
        whitePlayer.newGameClientNotifications(game);
    }
}