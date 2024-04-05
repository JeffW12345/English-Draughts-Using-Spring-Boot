package com.github.jeffw12345.draughts.models.messaging;

import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.move.Move;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ClientMessageToServer {
    private String clientId;
    private ClientToServerMessageType requestType;
    private Move move;
    private Colour colourOfClientPlayer;
}
