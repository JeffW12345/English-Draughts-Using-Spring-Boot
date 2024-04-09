package com.github.jeffw12345.draughts.server.messaging.io;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerToClientMessageType;

public class ServerMessageComposeService {

    public static void informClientsBoardUpdateTurnOngoing(String clientId, Board board) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(ServerToClientMessageType.UPDATE_BOARD_SAME_TURN)
                .board(board)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageBothClientsInAGame(messageAsJson, clientId);
    }

    public static void informClientsBoardUpdateTurnFinished(String clientId, Board board) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(ServerToClientMessageType.UPDATE_BOARD_CHANGE_OF_TURN)
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
                .serverResponseType(ServerToClientMessageType.DECLINE_MOVE)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.sendJsonMessage(messageAsJson, idOfClientSubmittingMove);
    }

    public static void informOtherClientOfResignation(String resigningClientId) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(ServerToClientMessageType.INFORM_OTHER_PLAYER_RESIGNED)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageOtherClientInGame(messageAsJson, resigningClientId);
    }

    public static void tellOtherClientDrawAccepted(String clientAcceptingDrawId) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(ServerToClientMessageType.INFORM_OF_DRAW_ACCEPTED)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageOtherClientInGame(messageAsJson, clientAcceptingDrawId);
    }

    public static void tellOtherClientDrawAOffered(String clientOfferingDrawId) {
        ServerMessageToClient serverMessageToClient = ServerMessageToClient.builder()
                .serverResponseType(ServerToClientMessageType.INFORM_DRAW_OFFER_MADE)
                .build();

        String messageAsJson = ServerMessagingUtility.convertServerMessageToJSON(serverMessageToClient);

        ServerMessagingOutboundService.messageOtherClientInGame(messageAsJson, clientOfferingDrawId);
    }
}