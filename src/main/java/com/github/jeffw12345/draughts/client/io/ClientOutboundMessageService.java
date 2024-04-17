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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.jeffw12345.draughts.client.io.models.ClientToServerMessageType.*;

@Getter
@Setter
public class ClientOutboundMessageService {
    private Session session;
    private final Client client;
    private Logger log;
    public ClientOutboundMessageService(Client client){
        this.client = client;
        log = LoggerFactory.getLogger(ClientOutboundMessageService.class);
    }

    public void sendJsonMessageToServer(String jsonMessage) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(jsonMessage);
            String errorMessage = String.format("Client %s sent message to server: %s", client.getClientId(), jsonMessage);
            log.info(errorMessage);
        } else {
            log.error("Cannot send message: Session object is null or closed");
        }
    }

    public void convertMessageToJSONThenSendToServer(ClientMessageToServer clientMessageToServer) {
        String messageAsJSON = ClientMessagingUtility.convertClientMessageToJSON(clientMessageToServer);
        sendJsonMessageToServer(messageAsJSON);
    }

    public void sendMoveToServer(Move move) {
        if (client != null && client.getClientController() != null) {
            ClientMessageToServer moveRequest = ClientMessageToServer.builder()
                    .clientId(client.getClientId())
                    .move(move)
                    .colourOfClientPlayer(client.getClientController().isAmIRed() ? Colour.RED : Colour.WHITE)
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
