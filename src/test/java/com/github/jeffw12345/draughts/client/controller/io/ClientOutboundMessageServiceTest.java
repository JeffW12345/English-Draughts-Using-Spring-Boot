package com.github.jeffw12345.draughts.client.controller.io;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.io.ClientOutboundMessageService;
import com.github.jeffw12345.draughts.game.models.move.Move;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import static org.mockito.ArgumentMatchers.any;

public class ClientOutboundMessageServiceTest {

    @Mock
    private Client client;

    @Mock
    private Session session;

    private ClientOutboundMessageService service;

    @Mock
    private Logger logger;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ClientOutboundMessageService(client);
        logger = mock(Logger.class);
        service.setLog(logger);
    }

    @Test
    public void sendJsonMessageToServer_givenSessionNull_issuesErrorLog() {
        service.setSession(null);
        service.sendJsonMessageToServer("message");
        verify(logger).error(any());
    }

    @Test
    public void sendJsonMessageToServer_givenClientControllerObjectNull_issuesErrorLog() {
        when(session.isOpen()).thenReturn(false);
        service.setSession(session);
        service.sendJsonMessageToServer("message");

        verify(logger).error(any());
    }

    @Test
    public void sendJsonMessageToServer_givenSessionObjectNonNullAndOpen_issuesInfoLogMessage(){
        RemoteEndpoint.Async asyncRemoteMock = mock(RemoteEndpoint.Async.class);

        when(session.isOpen()).thenReturn(true);
        when(session.getAsyncRemote()).thenReturn(asyncRemoteMock);

        service.setSession(session);

        when(client.getClientId()).thenReturn("ABC123");

        service.sendJsonMessageToServer("message");

        verify(logger).info("Client ABC123 sent message to server: message");
    }



    @Test
    public void convertMessageToJSONThenSendToServer_givenClientMessageToServerPassedIn_returnsValidJsonForThatObject(){
        //TODO
    }



    @Test
    public void sendMoveToServer_givenClientObjectNull_issuesErrorLevelLog(){
        service = new ClientOutboundMessageService(null);
        service.setLog(logger);
        service.sendMoveToServer(new Move());
        verify(logger).error(any());
    }

    @Test
    public void sendMoveToServer_givenClientControllerNull_issuesErrorLevelLog(){
        when(client.getClientController()).thenReturn(null);

        service.sendMoveToServer(new Move());

        verify(logger).error(any());
    }


    @Test
    public void sendMoveToServer_validMoveObject_convertMessageToSendableObject(){
        //TODO
    }

    @Test
    public void establishSession_successfulConnection_log_output_message_as_expected(){
        //TODO
    }

    @Test
    public void establishSession_successfulConnection_log_output_of_type_info(){
        //TODO
    }

    @Test
    public void establishSession_unsuccessfulConnection_log_output_message_as_expected(){
        //TODO
    }

    @Test
    public void establishSession_unsuccessfulConnection_log_output_of_type_error(){
        //TODO
    }


    @Test
    public void closeSession_givenSessionObjectNull_logsExpectedMessage(){
        //TODO
    }

    @Test
    public void closeSession_givenSessionObjectNull_issuesWarnLevelLog(){
        //TODO
    }

    @Test
    public void closeSession_givenSessionObjectNotOpen_logsExpectedMessage(){
        //TODO
    }

    @Test
    public void closeSession_givenSessionObjectNotOpen_issuesWarnLevelLog(){
        //TODO
    }

    @Test
    public void closeSession_givenIOExceptionThrown_issuesErrorLevelLog(){
        //TODO
    }

    @Test
    public void closeSession_givenIOExceptionThrown_issuesExpectedErrorMessage(){
        //TODO
    }
}
