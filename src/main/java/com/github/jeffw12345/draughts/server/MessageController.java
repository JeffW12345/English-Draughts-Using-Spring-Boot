package com.github.jeffw12345.draughts.server;

import com.github.jeffw12345.draughts.server.mapping.ClientIdToSessionMapping;
import com.github.jeffw12345.draughts.models.client.message.ClientRequestToServer;
import com.github.jeffw12345.draughts.models.client.message.ClientToServerRequestType;

public class MessageController {
    public void processMessageFromClient(ClientRequestToServer clientRequestToServer) {
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
            case ESTABLISH_CONNECTION:
                establishConnectionActions(clientRequestToServer);
                break;
            default:
                throw new IllegalArgumentException("Unexpected response type: " + clientToServerRequestType);
        }
    }

    private void establishConnectionActions(ClientRequestToServer clientRequestToServer) {
        ClientIdToSessionMapping.add(clientRequestToServer.getClientId(),
                clientRequestToServer.getClient().getClientMessagingService().getSession());
    }

    private void checkForUpdateActions(ClientRequestToServer clientRequestToServer) {
    }
    private void exitActions(ClientRequestToServer clientRequestToServer) {
    }

    private void acceptGameActions(ClientRequestToServer clientRequestToServer) {
    }

    private void resignationActions(ClientRequestToServer clientRequestToServer) {
    }

    private void drawAcceptActions(ClientRequestToServer clientRequestToServer) {
    }

    private void drawOfferActions(ClientRequestToServer clientRequestToServer) {
    }

    private void moveRequestActions(ClientRequestToServer clientRequestToServer) {
    }

    private void wantGameActions(ClientRequestToServer clientRequestToServer) {
    }

}