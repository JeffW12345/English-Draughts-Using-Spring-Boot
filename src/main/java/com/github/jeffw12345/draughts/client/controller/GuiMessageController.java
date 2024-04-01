package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.view.DraughtsBoardGui;

import javax.swing.*;

public class GuiMessageController {
    DraughtsBoardGui view;
    boolean amIRed;
    boolean isRedsTurn;
    public GuiMessageController(MasterClientController client){
        this.view = client.getView();
        this.amIRed = client.isAmIRed();
        this.isRedsTurn = client.isRedsTurn();
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

    void offerDrawButtonPressedMessage() {
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

    void whiteMoveOverMessage() {
        view.setTopLineMessageText("White's move is over.");
        view.setMiddleLineMessageText("It's now red's turn.");
        view.setBottomLineMessageText("");
        view.updateLabels();
    }

    void redMoveOverMessage() {
        view.setTopLineMessageText("Red's move is over.");
        view.setMiddleLineMessageText("It's now white's turn.");
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

    void ifDrawOfferMadeByOtherClientMessage() {
        view.setTopLineMessageText("You've been offered a draw.");
        view.setMiddleLineMessageText("Click the button to accept.");
        view.setBottomLineMessageText("");
        view.updateLabels();
    }

    void ifDrawOfferAcceptedByOtherClientMessage() {
        view.setTopLineMessageText("Your offer was accepted!");
        view.setMiddleLineMessageText("It's a draw.");
        view.setBottomLineMessageText("");
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
}
