package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.messaging.ServerResponseType;

public class WinLossController {
    private MasterClientController controller;

    public WinLossController(MasterClientController controller) {
        this.controller = controller;
    }

    void winnerActions(Colour winnerColour) {
        if(winnerColour == Colour.RED){
            viewUpdateIfWhiteLost();
        } else{
            viewUpdateIfRedLost();
        }
    }

    public void otherPlayerResignationActions() {
        // Implementation
    }

    public void viewUpdateIfRedLost() {
        // Implementation
    }

    public void viewUpdateIfWhiteLost() {
        // Implementation
    }

    public void resignationActions(Colour white) {
    }
}
