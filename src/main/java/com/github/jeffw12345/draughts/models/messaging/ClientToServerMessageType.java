package com.github.jeffw12345.draughts.models.messaging;

public enum ClientToServerMessageType {
    WANT_GAME,
    MOVE_REQUEST,
    DRAW_OFFER,
    DRAW_ACCEPT,
    RESIGN,
    EXITING_DUE_TO_GUI_CLOSE
}
