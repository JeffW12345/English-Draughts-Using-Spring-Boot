package com.github.jeffw12345.draughts.client.io;

import com.github.jeffw12345.draughts.client.io.models.ClientToServerMessageType;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.move.Move;

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
