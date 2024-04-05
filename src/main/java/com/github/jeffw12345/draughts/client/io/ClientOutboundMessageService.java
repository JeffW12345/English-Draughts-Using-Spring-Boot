package com.github.jeffw12345.draughts.client.io;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.io.models.ClientMessageToServer;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.move.Move;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.jeffw12345.draughts.client.io.models.ClientToServerMessageType.*;

@Slf4j
@Getter
public class ClientOutboundMessageService {
    private Session session;
    private final Client client;

    public ClientOutboundMessageService(Client client) {
        this.client = client;
    }

    public void sendJsonMessageToServer(String jsonMessage) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(jsonMessage);
            log.info("Client {} sent message to server: {}", client.getClientId(), jsonMessage);
        } else {
            log.warn("Cannot send message: session is closed");
            // Handle the situation gracefully, such as by logging a warning or taking alternative action.
        }
    }

    public void convertMessageToJSONThenSendToServer(ClientMessageToServer messageAsObject) {
        String messageAsJSON = ClientMessagingUtility.convertClientMessageToJSON(messageAsObject);
        sendJsonMessageToServer(messageAsJSON);
    }

    public void sendMoveToServer(Move move) {
        if (client != null && client.getClientController() != null) {
            Colour clientPlayerColour = client.getClientController().isAmIRed() ? Colour.RED : Colour.WHITE;
            ClientMessageToServer moveRequest = ClientMessageToServer.builder()
                    .clientId(client.getClientId())
                    .move(move)
                    .colourOfClientPlayer(clientPlayerColour)
                    .requestType(MOVE_REQUEST)
                    .build();

            convertMessageToJSONThenSendToServer(moveRequest);
        } else {
            log.error("Client or ClientController is null");
        }
    }

    public void sendOfferNewGameRequest(String clientId) {
        ClientMessageToServer requestForGame = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(WANT_GAME)
                .build();

        convertMessageToJSONThenSendToServer(requestForGame);
    }

    public void sendDrawOfferAcceptance(String clientId) {
        ClientMessageToServer acceptDrawMessage = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(DRAW_ACCEPT)
                .build();

        convertMessageToJSONThenSendToServer(acceptDrawMessage);
    }

    public void sendDrawOfferProposal(String clientId) {
        ClientMessageToServer offerDrawMessage = ClientMessageToServer.builder()
                .clientId(clientId)
                .requestType(DRAW_OFFER)
                .build();

        convertMessageToJSONThenSendToServer(offerDrawMessage);
    }

    public void sendResignation(String resigningClientId) {
        ClientMessageToServer resignationRequest = ClientMessageToServer.builder()
                .clientId(resigningClientId)
                .requestType(RESIGN)
                .build();

        convertMessageToJSONThenSendToServer(resignationRequest);
    }

    public void establishSession() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI("ws://localhost:8080/webSocket"));
            log.info("WebSocket session established");
        } catch (IOException | DeploymentException | URISyntaxException e) {
            log.error("Error establishing WebSocket session: {}", e.getMessage());
            // Handle the error gracefully, such as by retrying or notifying the user.
        }
    }

    public void sendGuiCloseMessageAndCloseSession(String windowClosedClientId) {
        ClientMessageToServer guiCloseMessage = ClientMessageToServer.builder()
                .clientId(windowClosedClientId)
                .requestType(EXITING_DUE_TO_GUI_CLOSE)
                .build();

        convertMessageToJSONThenSendToServer(guiCloseMessage);
        closeSession();
    }

    public void closeSession() {
        if (session != null && session.isOpen()) {
            try {
                log.warn("Closing communication session with server. You will receive a confirmation message.");
                session.close();
            } catch (IOException e) {
                log.error("Problem closing session: {}", e.getMessage());
                // Handle the error gracefully, such as logging or retrying.
            }
        }
    }
}
