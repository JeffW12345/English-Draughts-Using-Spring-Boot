package com.github.jeffw12345.draughts.server.messaging.io;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;
import com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType;

import static com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType.DECLINE_MOVE;
import static com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType.INFORM_DRAW_OFFER_MADE;
import static com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType.INFORM_OF_DRAW_ACCEPTED;
import static com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType.INFORM_OTHER_CLIENT_CLOSED_WINDOW;
import static com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType.INFORM_OTHER_PLAYER_RESIGNED;
import static com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType.UPDATE_BOARD_CHANGE_OF_TURN;
import static com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType.UPDATE_BOARD_SAME_TURN;

public class ServerMessageComposeService {

    public static void informClientsBoardUpdateTurnOngoing(String clientId, Board board) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(UPDATE_BOARD_SAME_TURN)
                .board(board)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageBothClientsInAGame(messageAsJson, clientId);
    }

    public static void informClientsBoardUpdateTurnFinished(String clientId, Board board) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(UPDATE_BOARD_CHANGE_OF_TURN)
                .board(board)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageBothClientsInAGame(messageAsJson, clientId);
    }

    public static void informClientsOfNewBoardAndThatGameWon(String clientId, Board board, Colour winningColour) {
        ServerToClientMessageType serverToClientMessageType =
                winningColour == Colour.RED ?
                        ServerToClientMessageType.INFORM_RED_IS_WINNER :
                        ServerToClientMessageType.INFORM_WHITE_IS_WINNER;

        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(serverToClientMessageType)
                .board(board)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageBothClientsInAGame(messageAsJson, clientId);
    }

    public static void informClientThatMoveIllegal(String idOfClientSubmittingMove) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(DECLINE_MOVE)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.sendJsonMessage(messageAsJson, idOfClientSubmittingMove);
    }

    public static void tellOtherClientAboutShutDown(String idOfClientWhoClosedGui){
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(INFORM_OTHER_CLIENT_CLOSED_WINDOW)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageOtherClientInGame(messageAsJson, idOfClientWhoClosedGui);
    }

    public static void informOtherClientOfResignation(String resigningClientId) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(INFORM_OTHER_PLAYER_RESIGNED)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageOtherClientInGame(messageAsJson, resigningClientId);
    }

    public static void tellOtherClientDrawAccepted(String clientAcceptingDrawId) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(INFORM_OF_DRAW_ACCEPTED)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageOtherClientInGame(messageAsJson, clientAcceptingDrawId);
    }

    public static void tellOtherClientDrawAOffered(String clientOfferingDrawId) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(INFORM_DRAW_OFFER_MADE)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageOtherClientInGame(messageAsJson, clientOfferingDrawId);
    }
}
