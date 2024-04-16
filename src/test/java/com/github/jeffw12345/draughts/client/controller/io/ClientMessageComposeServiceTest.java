package com.github.jeffw12345.draughts.client.controller.io;

import static org.mockito.Mockito.*;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.io.ClientMessageComposeService;
import com.github.jeffw12345.draughts.client.io.ClientOutboundMessageService;
import com.github.jeffw12345.draughts.client.io.models.ClientToServerMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClientMessageComposeServiceTest {
    @Mock
    private Client client;

    @Mock
    private ClientOutboundMessageService outboundMessagingService;

    private ClientMessageComposeService messageComposeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        messageComposeService = new ClientMessageComposeService(client);
        when(client.getClientOutboundMessagingService()).thenReturn(outboundMessagingService);
    }

    @Test
    void testSendOfferNewGameRequest() {
        String clientId = "testClientId";

        messageComposeService.sendOfferNewGameRequest(clientId);

        verify(outboundMessagingService).convertMessageToJSONThenSendToServer
                        (argThat(msg -> msg.getRequestType() == ClientToServerMessageType.WANT_GAME &&
                        msg.getClientId().equals(clientId)
        ));
    }

    @Test
    void testSendDrawOfferAcceptance() {
        String clientId = "testClientId";

        messageComposeService.sendDrawOfferAcceptance(clientId);

        verify(outboundMessagingService).convertMessageToJSONThenSendToServer
                (argThat(msg -> msg.getRequestType() == ClientToServerMessageType.DRAW_ACCEPT &&
                        msg.getClientId().equals(clientId)
        ));
    }

    @Test
    void testSendDrawOfferProposal() {
        String clientId = "testClientId";

        messageComposeService.sendDrawOfferProposal(clientId);

        verify(outboundMessagingService).convertMessageToJSONThenSendToServer
                (argThat(msg -> msg.getRequestType() == ClientToServerMessageType.DRAW_OFFER &&
                        msg.getClientId().equals(clientId)
        ));
    }

    @Test
    void testSendResignation() {
        String clientId = "testClientId";

        messageComposeService.sendResignation(clientId);

        verify(outboundMessagingService).convertMessageToJSONThenSendToServer(argThat(msg ->
                msg.getRequestType() == ClientToServerMessageType.RESIGN &&
                        msg.getClientId().equals(clientId)
        ));
    }
}
