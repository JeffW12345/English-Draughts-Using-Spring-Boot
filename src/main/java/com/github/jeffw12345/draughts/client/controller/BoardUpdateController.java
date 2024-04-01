package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;

public class BoardUpdateController {
    private MasterClientController controller;
    public BoardUpdateController(MasterClientController controller) {
        this.controller = controller;
    }
    public void updateBoardChangeOfTurn(ServerMessageToClient message) {
    }
    public void updateBoardSameTurn(ServerMessageToClient message) {
    }
}
