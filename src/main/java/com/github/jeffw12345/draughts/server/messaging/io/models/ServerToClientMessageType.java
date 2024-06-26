package com.github.jeffw12345.draughts.server.messaging.io.models;

public enum ServerToClientMessageType {
    ASSIGN_RED_COLOUR,
    ASSIGN_WHITE_COLOUR,
    UPDATE_BOARD_SAME_TURN,
    UPDATE_BOARD_CHANGE_OF_TURN,
    DECLINE_MOVE,
    INFORM_RED_IS_WINNER,
    INFORM_WHITE_IS_WINNER,
    INFORM_OF_DRAW_ACCEPTED,
    INFORM_OTHER_PLAYER_RESIGNED,
    INFORM_DRAW_OFFER_MADE,
    INFORM_CLIENT_OF_ID
}
