package com.github.jeffw12345.draughts.client.io;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.io.models.ClientMessageToServer;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.move.Move;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.jeffw12345.draughts.client.io.models.ClientToServerMessageType.*;

@Slf4j
@Getter
@Setter
@Builder
public class ClientOutboundMessageService {
    private Session session;
    private final Client client;
    private final Logger logger;

    public void sendJsonMessageToServer(String jsonMessage) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(jsonMessage);
            log.info("Client {} sent message to server: {}", client.getClientId(), jsonMessage);
        } else {
            log.warn("Cannot send message: session is closed");
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

    public void establishSession() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(client.getClientInboundMessagingService(),
                    new URI("ws://localhost:8080/webSocket"));
            log.info("WebSocket session established");
        } catch (IOException | DeploymentException | URISyntaxException e) {
            log.error("Error establishing WebSocket session: {}", e.getMessage());
        }
    }

    public void closeSession() {
        if (session != null && session.isOpen()) {
            try {
                log.warn("Closing communication session with server.");
                session.close();
            } catch (IOException e) {
                log.error("Problem closing session: {}", e.getMessage());
            }
        }
    }
}
