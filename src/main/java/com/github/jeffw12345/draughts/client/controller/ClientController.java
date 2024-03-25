package com.github.jeffw12345.draughts.client.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import com.github.jeffw12345.draughts.client.view.DraughtsBoardView;
import javax.swing.JOptionPane;

public class ClientController implements WindowListener {

    private final DraughtsBoardView view;

    private boolean drawOfferSentPending, drawOfferReceivedPending, isRedsTurn = true, amIRed,
            bothClientsConnectedToServer;
    private boolean newGameAgreedByPlayers;

    // TODO - How are turns being updated?

    // Todo - Inform client of game id once game is agreed. Then create thread to poll server for board updates, turn updates,
    // game status updates, draw offer updates, etc, every 100 ms.

    // TODO - Code to assign player red or white player, and player name, i.e. Player 1 and Player 2

    // TODO - StringWorker

    public ClientController() {
        view = new DraughtsBoardView(this);
    }

    public void acceptDrawButtonPressed() {
        //TODO - Message to server
        view.getAcceptDrawButton().setEnabled(false);
        view.getOfferNewGameButton().setEnabled(true);
        newGameAgreedByPlayers = false;// To prevent further moves
        acceptDrawButtonPressedMsg();
    }

    String acceptMsg() {
        return "Do you accept the offer?";
    }

    public void acceptNewGameButtonPressed() {
        //TODO - Message to server
        view.getAcceptNewGameButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(true);
        view.getResignButton().setEnabled(true);
        newGameAgreedByPlayers = true;
        acceptNewGameButtonPressedMsg();
    }

    String colourMessage() {
        if (amIRed) {
            return "You are the red player";
        } else {
            return "You are the white player";
        }
    }

    public boolean isDrawOfferReceivedPending() {
        return drawOfferReceivedPending;
    }

    public boolean isDrawOfferSentPending() {
        return drawOfferSentPending;
    }

    public boolean isRedsTurn() {
        return isRedsTurn;
    }

    public void offerDrawButtonPressed() {
        //TODO - Message to server
        view.getOfferDrawButton().setEnabled(false);
        drawOfferSentPending = true;
        offerDrawButtonPressedMsg();
    }

    public void offerNewGameButtonPressed() {
        //TODO - Message to server
        view.getOfferNewGameButton().setEnabled(false);
        offerNewGameButtonPressedMsg();

    }

    public void resignButtonPressed() {
        //TODO - Message to server
        view.getOfferNewGameButton().setEnabled(true);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        newGameAgreedByPlayers = false;// To prevent further moves
        resignButtonPressedMsg();
    }

    public void setDrawOfferReceivedPending(boolean drawOfferReceivedPending) {
        this.drawOfferReceivedPending = drawOfferReceivedPending;
    }

    public void setDrawOfferSentPending(boolean drawOfferSentPending) {
        this.drawOfferSentPending = drawOfferSentPending;
    }

    public void setPlayerColour() {
        // TODO - Set amIRed using response from server.
    }

    public void setRedsTurn(boolean isRedsTurn) {
        this.isRedsTurn = isRedsTurn;
    }

    // This method responds sends squares clicked data to the server.
    // In addition, it reduces excess messages to server that could
    // potentially cause race conditions by creating a pop up box
    // if the user clicks a square out of turn or before they have
    // agreed a game with their opponent.

    public void squareClicked(int column, int row) {

        if (!newGameAgreedByPlayers) {
            JOptionPane.showMessageDialog(view.getFrame(), "You need to agree a game before playing.");
            return;
        }
        if (amIRed && !isRedsTurn) {
            JOptionPane.showMessageDialog(view.getFrame(), "It is not your turn, it is white's turn");
            return;
        }
        if (!amIRed && isRedsTurn) {
            JOptionPane.showMessageDialog(view.getFrame(), "It is not your turn, it is red's turn");
            return;
        }

        //TODO - Inform server of moves
    }

    String turnMsg() {
        if (amIRed && isRedsTurn) {
            return "It is your turn.";
        }
        if (!amIRed && !isRedsTurn) {
            return "It is your turn.";
        } else {
            return "It's the other player's turn.";
        }
    }

    void updateBoard(String boardRepresentation) {
        //TODO - Rewrite this method
        String[] parsed = boardRepresentation.split(",");
        // squares starts at one as the first entry in the string is 'board'
        for (int squares = 1; squares < parsed.length; squares++) {
            // To convert the position in the array into co-ordinates
            // Top left corner is col 0, row 0.
            int col = (squares - 2) % 8;
            int row = (squares - 2) / 8;

            if (parsed[squares].equals("null")) {
                view.setBlank(col, row);
            }
            if (parsed[squares].equals("red_man")) {
                //view.addRedMan(col, row);
            }
            if (parsed[squares].equals("white_man")) {
                //view.addWhiteMan(col, row);
            }
            if (parsed[squares].equals("red_king")) {
                //view.addRedKing(col, row);
            }
            if (parsed[squares].equals("white_king")) {
                //view.addWhiteKing(col, row);
            }
        }
        view.getFrame().repaint();
        view.getFrame().setVisible(true);
    }

    public void ifSentDrawOfferExpires() {
        setDrawOfferSentPending(false);
        view.getOfferNewGameButton().setEnabled(false);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(true);
        view.getOfferDrawButton().setEnabled(true);
        ifWhiteLostMsg();
    }

    public void ifRedLost() {
        view.getOfferNewGameButton().setEnabled(true);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        newGameAgreedByPlayers = false; // To prevent further moves
        ifRedLostMsg();
    }

    public void ifWhiteLost() {
        view.getOfferNewGameButton().setEnabled(true);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        newGameAgreedByPlayers = false; // To prevent further moves
        ifWhiteLostMsg();
    }

    public void ifNewGameOfferAcceptedByOtherClient() {
        view.getResignButton().setEnabled(true);
        view.getOfferDrawButton().setEnabled(true);
        view.getOfferNewGameButton().setEnabled(false);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        isRedsTurn = true; // Red starts first in new games.
        drawOfferSentPending = false; // To cancel any pending offers.
        drawOfferReceivedPending = false;
        newGameAgreedByPlayers = true;
        ifNewGameOfferAcceptedByOtherClientMsg();
    }

    public void ifNewGameOfferReceived() {
        view.getAcceptNewGameButton().setEnabled(true);
        view.getOfferNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        ifNewGameOfferReceivedMsg();
    }

    public void ifOtherPlayerResigns() {
        view.getOfferNewGameButton().setEnabled(true);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        newGameAgreedByPlayers = false; // To prevent further moves
        ifOtherPlayerResignsMsg();
    }

    public void ifDrawOfferAcceptedByOtherClient() {
        view.getOfferNewGameButton().setEnabled(true);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        newGameAgreedByPlayers = false; // To prevent further moves
        ifDrawOfferAcceptedByOtherClientMsg();
    }

    public void ifDrawOfferMadeByOtherClient() {
        setDrawOfferReceivedPending(true);
        view.getAcceptDrawButton().setEnabled(true);
        view.getOfferDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(false);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getOfferNewGameButton().setEnabled(false);
        ifDrawOfferMadeByOtherClientMsg();
    }

    public void ifReceivedDrawOfferExpires() {
        ifReceivedDrawOfferExpiresMsg();
        setDrawOfferReceivedPending(false);
        view.getAcceptDrawButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(true);
        view.getResignButton().setEnabled(true);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getOfferNewGameButton().setEnabled(false);
        ifReceivedDrawOfferExpiresMsg();
    }

    public boolean isAmIRed() {
        return amIRed;
    }

    public void setAmIRed(boolean amIRed) {
        this.amIRed = amIRed;
    }

    public void setWelcomeMessage() {

        view.setTopLineMessage("Welcome to English Draughts!");
        view.setMiddleLineMessage("");
        view.setBottomLineMessage(colourMessage());
    }

    public void ifReceivedDrawOfferExpiresMsg() {
        view.setTopLineMessage("The draw offer has expired.");
        view.setMiddleLineMessage(turnMsg());
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public void ifDrawOfferMadeByOtherClientMsg() {
        view.setTopLineMessage("You've been offered a draw.");
        view.setMiddleLineMessage("Click the button to accept.");
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public void ifDrawOfferAcceptedByOtherClientMsg() {
        view.setTopLineMessage("Your offer was accepted!");
        view.setMiddleLineMessage("It's a draw.");
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public void ifOtherPlayerResignsMsg() {
        view.setTopLineMessage("Your opponent has resigned!");
        view.setMiddleLineMessage("You've won - Well done.");
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public void ifNewGameOfferReceivedMsg() {
        view.setTopLineMessage("Your opponent has offered a game.");
        view.setMiddleLineMessage("Click the button to accept.");
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public void ifNewGameOfferAcceptedByOtherClientMsg() {
        view.setTopLineMessage("The other client has accepted.");
        view.setMiddleLineMessage("Let battle commence!");
        view.setBottomLineMessage(colourMessage());
        view.updateLabels();
    }

    public void ifWhiteLostMsg() {
        if (amIRed) {
            view.setTopLineMessage("Well done! You've won");
            view.setMiddleLineMessage("");
            view.setBottomLineMessage("");
            view.updateLabels();
        } else {
            view.setTopLineMessage("I'm afraid you've lost.");
            view.setMiddleLineMessage("Better luck next time.");
            view.setBottomLineMessage("");
            view.updateLabels();
        }

    }

    public void ifRedLostMsg() {
        if (!amIRed) {
            view.setTopLineMessage("Well done! You've won");
            view.setMiddleLineMessage("");
            view.setBottomLineMessage("");
            view.updateLabels();
        } else {
            view.setTopLineMessage("I'm afraid you've lost.");
            view.setMiddleLineMessage("Better luck next time.");
            view.setBottomLineMessage("");
            view.updateLabels();
        }
    }

    public void offerDrawButtonPressedMsg() {
        view.setTopLineMessage("You've offered a draw.");
        view.setMiddleLineMessage("Waiting for a response...");
        view.setBottomLineMessage("Expires end of turn.");
        view.updateLabels();
    }

    public void resignButtonPressedMsg() {
        view.setTopLineMessage("You have resigned.");
        view.setMiddleLineMessage("Better luck next time.");
        view.setBottomLineMessage("Press 'New Game' to play again.");
        view.updateLabels();
    }

    public void offerNewGameButtonPressedMsg() {
        view.setTopLineMessage("You've offered a new game");
        view.setMiddleLineMessage("Waiting for your opponent.");
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public void acceptNewGameButtonPressedMsg() {
        view.setTopLineMessage("You've accepted a new game.");
        view.setMiddleLineMessage("Good luck!");
        view.setBottomLineMessage(colourMessage());
        view.updateLabels();
    }

    public void acceptDrawButtonPressedMsg() {
        view.setTopLineMessage("You have agreed to a draw.");
        view.setMiddleLineMessage("The game is over.");
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public void stillRedMoveMsg() {
        view.setTopLineMessage("It is still red's move");
        view.setMiddleLineMessage("There is a jump available.");
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public void stillWhiteMoveMsg() {
        view.setTopLineMessage("It is still white's move");
        view.setMiddleLineMessage("There is a jump available.");
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public void whiteMoveOverMsg() {
        view.setTopLineMessage("White's move is over.");
        view.setMiddleLineMessage("It's now red's turn.");
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public void redMoveOverMsg() {
        view.setTopLineMessage("Red's move is over.");
        view.setMiddleLineMessage("It's now white's turn.");
        view.setBottomLineMessage("");
        view.updateLabels();
    }

    public boolean isBothPlayersReady() {
        return bothClientsConnectedToServer;
    }

    public void setBothPlayersReady(boolean bothPlayersReady) {
        bothClientsConnectedToServer = bothPlayersReady;
    }

    public void bothPlayersReadyActions() {
        if (bothClientsConnectedToServer) {
            view.initialSetup();
            view.setMiddleLineMessage("Both players are connected");
            if (amIRed) {
                view.setBottomLineMessage("You are the red player.");
            } else {
                view.setBottomLineMessage("You are the white player.");
            }
            view.updateLabels();
        }

    }

    public void invalidMove() {
        JOptionPane.showMessageDialog(view.getFrame(), "Invalid move. Please try again");
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        // TODO - Code to inform server and code for server to take action. Need to wait for other client to contact us?
        // List of checks with every communication?
        // clientThread.out.println(clientThread.nextMessageId() + "," + "end");
        System.exit(0);
        //TODO - Simplify with lambda call frame.addWindowListener(new WindowAdapter() {
        //    @Override
        //    public void windowClosing(WindowEvent e) {
        //        // Your code for closing window
        //    }
        //});
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

}