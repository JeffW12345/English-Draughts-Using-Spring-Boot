package com.github.jeffw12345.draughts.client.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.CountDownLatch;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.service.ClientMessagingUtility;
import com.github.jeffw12345.draughts.client.view.DraughtsBoardView;
import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import com.github.jeffw12345.draughts.models.client.request.ClientRequestToServer;

import com.github.jeffw12345.draughts.models.response.ServerResponseToClient;
import com.github.jeffw12345.draughts.models.response.ServerResponseType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.JOptionPane;

import static com.github.jeffw12345.draughts.models.client.request.ClientToServerRequestType.WANT_GAME;
import static com.github.jeffw12345.draughts.models.client.request.ClientToServerRequestType.WANT_PLAYER_ID;

@Getter
@Setter
@Slf4j
public class ClientController implements WindowListener {
    private Client client;
    private final DraughtsBoardView view =  new DraughtsBoardView(this);
    private final CountDownLatch playerIdAssignedLatch = new CountDownLatch(1);
    private boolean amIRed,
            gameInProgress,
            isRedsTurn = true,
            drawOfferSentPending,
            drawOfferReceivedPending,
            bothClientsConnectedToServer;

    // TODO - StringWorker

    public ClientController(Client client) {
        this.client = client;
    }

    public void setUp()  {
        requestPlayerId();
        try{
            playerIdAssignedLatch.await();
        }
        catch (InterruptedException ex){
            log.error(ex.getMessage());
        }
        view.setUp();
    }

    private void requestPlayerId() {
        ClientRequestToServer requestForPlayerId = ClientRequestToServer.builder()
                .client(this.client)
                .requestType(WANT_PLAYER_ID)
                .build();

        String requestForPlayerIDAsJSON =
                ClientMessagingUtility.convertClientRequestToServerObjectToJSON(requestForPlayerId);

        this.client.getClientMessagingService().sendMessageToServer(requestForPlayerIDAsJSON);
    }

    public void processMessageFromServer(ServerResponseToClient serverResponseToClient) {
        ServerResponseType serverResponseType = serverResponseToClient.getServerResponseType();
        switch (serverResponseType) {
            case NO_UPDATE:
                noUpdateActions();
                break;
            case ASSIGN_PLAYER_ID:
                assignPlayerIdActions(serverResponseToClient);
                break;
            case ASSIGN_PLAYER_COLOUR_AND_GAME_ID:
                gameStartActions(serverResponseToClient);
                break;
            case DECLINE_MOVE:
                invalidMoveOptions();
                break;
            case INFORM_OF_DRAW_ACCEPTED:
                drawAcceptedActions(serverResponseToClient);
                break;
            case INFORM_OF_STALEMATE:
                stalemateActions();
                break;
            case INFORM_RED_IS_WINNER:
                winnerActions(Colour.RED);
                break;
            case INFORM_WHITE_IS_WINNER:
                winnerActions(Colour.WHITE);
                break;
            case UPDATE_BOARD_CHANGE_OF_TURN:
                updateBoardChangeOfTurn(serverResponseToClient);
                break;
            case UPDATE_BOARD_SAME_TURN:
                updateBoardSameTurn(serverResponseToClient);
                break;
            case INFORM_OF_PLAYER_RESIGNATION:
                playerResignationActions(serverResponseToClient);
                break;
            default:
                throw new IllegalArgumentException("Unexpected response type: " + serverResponseType);
        }
    }

    private void updateBoardSameTurn(ServerResponseToClient serverResponseObject) {
        Board newBoard = serverResponseObject.getGame().getCurrentBoard();
        repaintBoard(newBoard);
    }

    private void updateBoardChangeOfTurn(ServerResponseToClient serverResponseObject) {
        Board updatedBoard = serverResponseObject.getGame().getCurrentBoard();
        repaintBoard(updatedBoard);
        changeTurns();
    }

    private void winnerActions(Colour winnerColour) {
        if(winnerColour == Colour.RED){
            viewUpdateIfWhiteLost();
        } else{
            viewUpdateIfRedLost();
        }
    }
    private void stalemateActions() {
        stalemateViewUpdate();
    }

    private void drawAcceptedActions(ServerResponseToClient serverResponseObject) {
        drawOfferAcceptedViewUpdate();
    }

    private void assignPlayerIdActions(ServerResponseToClient serverResponseObject) {
        playerIdAssignedLatch.countDown();
    }

    private void noUpdateActions() {
    }

    private void playerResignationActions(ServerResponseToClient client) {
    }
    public void changeTurns() {
        this.drawOfferSentPending = false;
        this.drawOfferReceivedPending = false;
        this.isRedsTurn = !this.isRedsTurn;
    }



    public void offerNewGameButtonPressed() {
        view.getOfferNewGameButton().setEnabled(false);
        offerNewGameButtonPressedMsg();

        ClientRequestToServer requestForGame = ClientRequestToServer.builder()
                .client(client)
                .requestType(WANT_GAME)
                .build();



        //TODO - processMessageFromServer(requestForGame.makeServerRequestAndGetResponse());
    }

    public void acceptDrawButtonPressed() {
        //TODO - Message to server
        view.getAcceptDrawButton().setEnabled(false);
        view.getOfferNewGameButton().setEnabled(true);
        //newGameAgreedByPlayers = false;// To prevent further moves
        acceptDrawButtonPressedMsg();
    }

    String colourMessage() {
        if (amIRed) {
            return "You are the red player";
        } else {
            return "You are the white player";
        }
    }

    public void offerDrawButtonPressed() {
        //TODO - Message to server
        view.getOfferDrawButton().setEnabled(false);
        drawOfferSentPending = true;
        offerDrawButtonPressedMsg();
    }

    public void stalemateViewUpdate(){
        //TODO
    }

    public void resignButtonPressed() {
        //TODO - Message to server
        view.getOfferNewGameButton().setEnabled(true);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        gameInProgress = false;
        resignButtonPressedMsg();
    }


    public void setRedsTurn(boolean isRedsTurn) {
        this.isRedsTurn = isRedsTurn;
    }

    // This method responds sends squares clicked data to the server.
    // In addition, it reduces excess messages to server that could
    // potentially cause race conditions by creating a pop up box
    // if the user clicks a square out of turn or before they have
    // agreed a game with their opponent.

    // TODO - This.
    public void squareClicked(int column, int row) {

        if (!gameInProgress) {
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

    //TODO - Use same method to initialise board
    public void repaintBoard(Board board) {
        for(int row = 0; row < 8; row++){
            for(int column = 0; column < 8; column++){
                SquareContent squareContent = board.getSquareContentAtRowAndColumn(row, column);
                if(squareContent == SquareContent.RED_MAN){
                    view.addRedMan(column, row);
                }
                if(squareContent == SquareContent.WHITE_MAN){
                    view.addWhiteMan(column, row);
                }
                if(squareContent == SquareContent.RED_KING){
                    view.addRedKing(column, row);
                }
                if(squareContent == SquareContent.WHITE_KING){
                    view.addWhiteKing(column, row);
                }
                if(squareContent == SquareContent.EMPTY){
                    view.setBlank(column, row);
                }
            }
        }
        view.getFrame().repaint();
        view.getFrame().setVisible(true);
    }

    public void ifSentDrawOfferExpires() {
        //TODO - setDrawOfferSentPending(false);
        view.getOfferNewGameButton().setEnabled(false);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(true);
        view.getOfferDrawButton().setEnabled(true);
        ifWhiteLostMsg();
    }

    public void viewUpdateIfRedLost() {
        view.getOfferNewGameButton().setEnabled(true);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        gameInProgress = false; // To prevent further moves
        ifRedLostMsg();
    }

    public void viewUpdateIfWhiteLost() {
        view.getOfferNewGameButton().setEnabled(true);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        gameInProgress = false; // To prevent further moves
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
        gameInProgress = true;
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
        gameInProgress = false; // To prevent further moves
        ifOtherPlayerResignsMsg();
    }

    public void drawOfferAcceptedViewUpdate() {
        view.getOfferNewGameButton().setEnabled(true);
        view.getAcceptNewGameButton().setEnabled(false);
        view.getAcceptDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        gameInProgress = false; // To prevent further moves
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
            view.setUp();
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


    public void gameStartActions(ServerResponseToClient serverResponseObject) {
        boolean isClientWhitePlayer =
                serverResponseObject
                        .getGame()
                        .isPlayerWhitePlayer(serverResponseObject.getPlayer());

        setAmIRed(!isClientWhitePlayer);
        setGameInProgress(true);
        //TODO - Do I need to update the game id?
    }

    public void invalidMoveOptions() {
        // TODO - Move id system?
        invalidMove();
    }
}