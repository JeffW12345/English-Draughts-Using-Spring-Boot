package com.github.jeffw12345.draughts.models.messaging;

public enum ClientToServerMessageType {
    WANT_GAME, MOVE_REQUEST, DRAW_OFFER, DRAW_ACCEPT, RESIGN, ACCEPT_GAME, EXIT,
    ESTABLISH_SESSION
}
