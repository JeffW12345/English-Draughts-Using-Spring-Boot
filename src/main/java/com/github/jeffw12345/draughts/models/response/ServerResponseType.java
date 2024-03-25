package com.github.jeffw12345.draughts.models.response;

public enum ServerResponseType {

    ASSIGN_PLAYER_ID,
    ASSIGN_PLAYER_NAME,
    ALLOW_MOVE,
    UPDATE_BOARD,
    DECLINE_MOVE,
    INFORM_OF_WINNER,
    INFORM_OF_DRAW_ACCEPTED,
    INFORM_OF_STALEMATE,
    INFORM_OF_CHANGE_OF_TURN
}
