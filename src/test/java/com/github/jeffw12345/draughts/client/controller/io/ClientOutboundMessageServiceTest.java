package com.github.jeffw12345.draughts.client.controller.io;

import com.github.jeffw12345.draughts.client.io.ClientInboundMessageService;
import org.mockito.Mockito;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.io.ClientOutboundMessageService;
import com.github.jeffw12345.draughts.game.models.move.Move;

import jakarta.websocket.DeploymentException;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;

public class ClientOutboundMessageServiceTest {
    @Mock
    private WebSocketContainer mockContainer;
    @Mock
    private Client mockClient;
    @Mock
    private Session mockSession;
    @Mock
    private Logger mockLogger;
    private ClientOutboundMessageService clientOutboundMessageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clientOutboundMessageService = new ClientOutboundMessageService(mockClient);
        clientOutboundMessageService.setLog(mockLogger);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(mockContainer, mockClient, mockSession, mockLogger);
    }

    @Test
    public void sendJsonMessageToServer_givenSessionNull_issuesErrorLog() {
        clientOutboundMessageService.setSession(null);
        clientOutboundMessageService.sendJsonMessageToServer("message");
        verify(mockLogger).error(any());
    }

    @Test
    public void sendJsonMessageToServer_givenClientControllerObjectNull_issuesErrorLog() {
        when(mockSession.isOpen()).thenReturn(false);
        clientOutboundMessageService.setSession(mockSession);
        clientOutboundMessageService.sendJsonMessageToServer("message");

        verify(mockLogger).error(any());
    }

    @Test
    public void sendJsonMessageToServer_givenSessionObjectNonNullAndOpen_issuesInfoLogMessage(){
        RemoteEndpoint.Async asyncRemoteMock = mock(RemoteEndpoint.Async.class);

        when(mockSession.isOpen()).thenReturn(true);
        when(mockSession.getAsyncRemote()).thenReturn(asyncRemoteMock);

        clientOutboundMessageService.setSession(mockSession);

        when(mockClient.getClientId()).thenReturn("ABC123");

        clientOutboundMessageService.sendJsonMessageToServer("message");

        verify(mockLogger).info("Client ABC123 sent message to server: message");
    }

    @Test
    public void sendMoveToServer_givenClientObjectNull_issuesErrorLevelLog(){
        clientOutboundMessageService = new ClientOutboundMessageService(null);
        clientOutboundMessageService.setLog(mockLogger);
        clientOutboundMessageService.sendMoveToServer(new Move());
        verify(mockLogger).error(any());
    }

    @Test
    public void sendMoveToServer_givenClientControllerNull_issuesErrorLevelLog(){
        when(mockClient.getClientController()).thenReturn(null);

        clientOutboundMessageService.sendMoveToServer(new Move());

        verify(mockLogger).error(any());
    }

    @Test
    public void establishSession_noExceptionsThrown_log_output_of_type_info() {
        clientOutboundMessageService.establishSession(mockContainer);

        verify(mockLogger).info(any());
    }

    @Test
    public void establishSession_givenExceptionThrown_thenErrorLevelLog() throws Exception {
        when(mockClient.getClientInboundMessagingService()).thenReturn(new ClientInboundMessageService(mockClient));

        doThrow(new DeploymentException("Connection failed"))
                .when(mockContainer)
                .connectToServer(any(ClientInboundMessageService.class), any(URI.class));

        clientOutboundMessageService.establishSession(mockContainer);

        verify(mockLogger).error(any());
    }

    @Test
    public void closeSession_givenSessionObjectNull_issuesWarnLevelLog(){
        clientOutboundMessageService.setSession(null);

        clientOutboundMessageService.closeSession();

        verify(mockLogger).warn(any());
    }

    @Test
    public void closeSession_givenSessionObjectNotOpen_issuesWarnLevelLog(){
        when(mockSession.isOpen()).thenReturn(false);
        clientOutboundMessageService.setSession(mockSession);

        clientOutboundMessageService.closeSession();

        verify(mockLogger).warn(any());
    }

    @Test
    public void closeSession_givenIOExceptionThrown_issuesErrorLevelLog() throws IOException {
        when(mockSession.isOpen()).thenReturn(true);
        doThrow(new IOException("Failed to close")).when(mockSession).close();
        clientOutboundMessageService.setSession(mockSession);

        clientOutboundMessageService.closeSession();

        verify(mockLogger).error(any());
    }
}
