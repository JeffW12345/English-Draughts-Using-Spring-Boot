package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.view.DraughtsBoardGui;

import javax.swing.*;

public class GuiMessageController {
    private final DraughtsBoardGui view;
    private final MasterClientController masterClientController;
    public GuiMessageController(MasterClientController masterController){
        this.masterClientController = masterController;
        this.view = masterController.getView();
    }

    void ifWhiteLostMessage() {
        if (masterClientController.isAmIRed()) {
            view.setTopLineMessageText("Well done! You've won");
            view.setMiddleLineMessageText("");
            view.setBottomLineMessageText("");
            view.updateLabels();
        } else {
            view.setTopLineMessageText("I'm afraid you've lost.");
            view.setMiddleLineMessageText("Better luck next time.");
            view.setBottomLineMessageText("");
            view.updateLabels();
        }
    }

    void ifRedLostMessage() {
        if (!masterClientController.isAmIRed()) {
            view.setTopLineMessageText("Well done! You've won");
            view.setMiddleLineMessageText("");
            view.setBottomLineMessageText("");
            view.updateLabels();
        } else {
            view.setTopLineMessageText("I'm afraid you've lost.");
            view.setMiddleLineMessageText("Better luck next time.");
            view.setBottomLineMessageText("");
            view.updateLabels();
        }
    }

    void youHaveOfferedDrawMessage() {
        view.setTopLineMessageText("You've offered a draw.");
        view.setMiddleLineMessageText("Waiting for a response...");
        view.setBottomLineMessageText("Expires end of turn.");
        view.updateLabels();
    }

    void resignButtonPressedMessage() {
        view.setTopLineMessageText("You have resigned.");
        view.setMiddleLineMessageText("Better luck next time.");
        view.setBottomLineMessageText("Press 'New Game' to play again.");
        view.updateLabels();
    }

    void offerNewGameButtonPressedMessage() {
        view.setTopLineMessageText("You've offered a new game");
        view.setMiddleLineMessageText("Waiting for a match.");
        view.setBottomLineMessageText("");
        view.updateLabels();
    }

    void acceptDrawButtonPressedMessage() {
        view.setTopLineMessageText("You have agreed to a draw.");
        view.setMiddleLineMessageText("The game is over.");
        view.setBottomLineMessageText("");
        view.updateLabels();
    }
    
    void drawOfferExpiresMessage() {
        view.setTopLineMessageText("The draw offer has expired.");
        view.setMiddleLineMessageText(turnMessage());
        view.setBottomLineMessageText("");
        view.updateLabels();
    }

    String turnMessage() {
        if (masterClientController.isAmIRed() && masterClientController.isRedsTurn()) {
            return "It is your turn.";
        }
        if (!masterClientController.isAmIRed() && !masterClientController.isRedsTurn()) {
            return "It is your turn.";
        } else {
            return "It's the other player's turn.";
        }
    }

    void drawOfferMadeByOtherClientMessage() {
        view.setTopLineMessageText("You've been offered a draw.");
        view.setMiddleLineMessageText("Click the button to accept.");
        view.setBottomLineMessageText("");
        view.updateLabels();
    }

    void drawOfferAcceptedMessage() {
        view.setTopLineMessageText("The draw offer was accepted.");
        view.setMiddleLineMessageText("It's a draw.");
        view.setBottomLineMessageText("Game over.");
        view.updateLabels();
    }

    void ifOtherPlayerResignsMessage() {
        view.setTopLineMessageText("Your opponent has resigned!");
        view.setMiddleLineMessageText("You've won - Well done.");
        view.setBottomLineMessageText("Press offer new game for a new game");
        view.updateLabels();
    }

    private String colourMessage() {
        if (masterClientController.isAmIRed()) {
            return "You are the red player";
        } else {
            return "You are the white player";
        }
    }
    void invalidMovePopUpMessage() {
        JOptionPane.showMessageDialog(view.getFrame(), "Invalid move. Please try again");
    }

    void bothPlayersReadyMessage() {
        view.setTopLineMessageText("Both players are connected.");
        if (masterClientController.isAmIRed()) {
            view.setMiddleLineMessageText("You are the red player.");
        } else {
            view.setMiddleLineMessageText("You are the white player.");
        }
        view.setBottomLineMessageText("Good luck!");
        view.updateLabels();
    }

    void turnOverMessage(){
        if (masterClientController.isRedsTurn()){
            redTurnMessage();
        } else {
            whiteTurnMessage();
        }
    }

    private void redTurnMessage() {
        String topLine = masterClientController.isAmIRed() ? "You are red" : "You are white";
        String bottomLine = masterClientController.isAmIRed() ? "It's your turn." : "It's red's turn";

        view.setTopLineMessageText(topLine);
        view.setMiddleLineMessageText("");
        view.setBottomLineMessageText(bottomLine);
        view.updateLabels();
    }

    private void whiteTurnMessage() {
        String topLine = masterClientController.isAmIRed() ? "You are red" : "You are white";
        String bottomLine = masterClientController.isAmIRed() ? "It's white's turn." : "It's your turn";

        view.setTopLineMessageText(topLine);
        view.setMiddleLineMessageText("");
        view.setBottomLineMessageText(bottomLine);
        view.updateLabels();
    }


    public void turnOngoingMessage() {
        if (masterClientController.isRedsTurn()){
            redTurnOngoingMessage();
        } else {
            whiteTurnOngoingMessage();
        }
    }

    private void whiteTurnOngoingMessage() {
        view.setTopLineMessageText("White can overtake again.");
        view.setMiddleLineMessageText("White's move is still ongoing.");
        view.setBottomLineMessageText("");
        view.updateLabels();
    }

    private void redTurnOngoingMessage() {
        view.setTopLineMessageText("Red can overtake again.");
        view.setMiddleLineMessageText("Red's move is still ongoing.");
        view.setBottomLineMessageText("");
        view.updateLabels();
    }

    public void setWelcomeMessageWithColours() {
        view.setTopLineMessageText("Welcome to English Draughts!");
        view.setMiddleLineMessageText("");
        view.setBottomLineMessageText(colourMessage());
        view.updateLabels();
    }

    public void setWelcomeMessageWithoutColours() {
        view.setTopLineMessageText("Welcome to English Draughts!");
        view.setMiddleLineMessageText("");
        view.setBottomLineMessageText("Waiting for player assignment");
        view.updateLabels();
    }


}
