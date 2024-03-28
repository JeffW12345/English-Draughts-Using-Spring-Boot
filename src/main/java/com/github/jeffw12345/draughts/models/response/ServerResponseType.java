package com.github.jeffw12345.draughts.models.response;

public enum ServerResponseType {

    ASSIGN_PLAYER_ID,
    ASSIGN_PLAYER_COLOUR_AND_GAME_ID, //TODO - The name is derived from this.
    UPDATE_BOARD_SAME_TURN,
    UPDATE_BOARD_CHANGE_OF_TURN,
    DECLINE_MOVE,
    INFORM_RED_IS_WINNER,
    INFORM_WHITE_IS_WINNER,
    INFORM_OF_DRAW_ACCEPTED,
    INFORM_OF_STALEMATE,
    NO_UPDATE,
    INFORM_OF_PLAYER_RESIGNATION
}
