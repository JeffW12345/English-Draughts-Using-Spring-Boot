package com.github.jeffw12345.draughts.client.controller.io;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.controller.MasterClientController;
import com.github.jeffw12345.draughts.client.io.ClientInboundMessageService;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;
import jakarta.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ClientInboundMessageServiceTest {

    @Mock
    private Client mockClient;

    @Mock
    private Session mockSession;

    private ClientInboundMessageService clientInboundMessageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        clientInboundMessageService = new ClientInboundMessageService(mockClient);
    }

    @Test
    public void testOnClose() {
        clientInboundMessageService.onClose(mockSession);
        verify(mockClient, times(1)).getClientId();
    }


    @Test
    public void testOnMessage() {
        String jsonMessage = "{\"client_id\":\"testClientId\"}";
        MasterClientController mockClientController = mock(MasterClientController.class);
        when(mockClient.getClientController()).thenReturn(mockClientController);

        clientInboundMessageService.onMessage(jsonMessage);

        verify(mockClient, times(1)).getClientId();
        verify(mockClientController, times(1)).processMessageFromServer(any(ServerMessageToClient.class));
    }
}
