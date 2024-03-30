package com.github.jeffw12345.draughts.models.client.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.game.Player;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientRequestToServer {
    private String clientId;
    private Client client;
    private ClientToServerRequestType requestType;
    private Game game;
    private Player player;
    private Move move;

}