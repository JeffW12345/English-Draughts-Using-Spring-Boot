package com.github.jeffw12345.draughts.client.controller.io;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.jeffw12345.draughts.client.io.ClientMessagingUtility;
import com.github.jeffw12345.draughts.client.io.models.ClientMessageToServer;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClientMessagingUtilityTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ObjectWriter objectWriter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertClientMessageToJSON() throws Exception {
        // Arrange
        ClientMessageToServer clientMessage = ClientMessageToServer.builder()
                .clientId("testClientId")
                .build();

        String expectedJson = "{\n" +
                "  \"clientId\" : \"testClientId\",\n" +
                "  \"requestType\" : null,\n" +
                "  \"move\" : null,\n" +
                "  \"colourOfClientPlayer\" : null\n" +
                "}";

        when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(objectWriter);
        when(objectWriter.writeValueAsString(clientMessage)).thenReturn(expectedJson);

        String actualJson = ClientMessagingUtility.convertClientMessageToJSON(clientMessage);

        assertEquals(objectMapper.readTree(expectedJson), objectMapper.readTree(actualJson));
    }
    @Test
    void testGetServerMessageObjectFromJson() {
        String json = "{\n" +
                "  \"board_state\" : null,\n" +
                "  \"response_type\" : null,\n" +
                "  \"client_id\" : \"testClientId\"\n" +
                "}";

        ServerMessageToClient serverMessageObject = ClientMessagingUtility.getServerMessageObjectFromJson(json);

        assertInstanceOf(ServerMessageToClient.class, serverMessageObject);

        assertEquals("testClientId", serverMessageObject.getClientId());
    }
}
