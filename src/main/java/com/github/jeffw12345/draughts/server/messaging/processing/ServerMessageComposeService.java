package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;

public class ServerMessageComposeService {

    public static void informClientsOfNewBoardAndThatTurnOngoing(String clientId, Board board) {
        //TODO
    }

    public static void informClientsOfNewBoardAndThatTurnFinished(String clientId, Board board) {
        //TODO
    }

    public static void informClientsOfNewBoardAndThatGameWon(String clientId, Board board, Colour colour) {
    }

    public static void informClientThatMoveIllegal(String clientId) {
    }
}
