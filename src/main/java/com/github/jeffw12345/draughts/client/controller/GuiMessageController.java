package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.view.DraughtsBoardGui;

import javax.swing.*;

public class GuiMessageController {
    DraughtsBoardGui view;
    boolean amIRed;
    boolean isRedsTurn;
    public GuiMessageController(MasterClientController masterController){
        this.view = masterController.getView();
        this.amIRed = masterController.isAmIRed();
        this.isRedsTurn = masterController.isRedsTurn();
    }
    void ifWhiteLostMessage() {
        if (amIRed) {
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
        if (!amIRed) {
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
        view.setMiddleLineMessageText("Waiting for your opponent.");
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
        if (amIRed && isRedsTurn) {
            return "It is your turn.";
        }
        if (!amIRed && !isRedsTurn) {
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
        view.setBottomLineMessageText("");
        view.updateLabels();
    }

    public void setWelcomeMessage() {
        view.setTopLineMessageText("Welcome to English Draughts!");
        view.setMiddleLineMessageText("");
        view.setBottomLineMessageText(colourMessage(amIRed));
        view.updateLabels();
    }

    private String colourMessage(boolean amIRed) {
        if (amIRed) {
            return "You are the red player";
        } else {
            return "You are the white player";
        }
    }
    void invalidMovePopUpMessage() {
        JOptionPane.showMessageDialog(view.getFrame(), "Invalid move. Please try again");
    }

    void bothPlayersReadyMessage() {
        view.setMiddleLineMessageText("Both players are connected");
        if (amIRed) {
            view.setBottomLineMessageText("You are the red player.");
        } else {
            view.setBottomLineMessageText("You are the white player.");
        }
        view.updateLabels();
    }

    void turnOverMessage(){
        if (isRedsTurn){
            redMoveOverMessage();
        } else {
            whiteMoveOverMessage();
        }
    }

    private void whiteMoveOverMessage() {
        view.setTopLineMessageText("White's turn is over.");
        view.setMiddleLineMessageText("It's now red's turn.");
        view.setBottomLineMessageText("");
        view.updateLabels();
    }

    private void redMoveOverMessage() {
        view.setTopLineMessageText("Red's turn is over.");
        view.setMiddleLineMessageText("It's now white's turn.");
        view.setBottomLineMessageText("");
        view.updateLabels();
    }


    public void turnOngoingMessage() {
        if (isRedsTurn){
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
}
