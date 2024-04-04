package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.view.DraughtsBoardGui;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;
import com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;

@Getter
@Slf4j
//TODO - Check encapsulation as tight as possible.
public class MasterClientController {
    private final Client client;
    private final DraughtsBoardGui view = new DraughtsBoardGui(this);
    private boolean amIRed, gameInProgress, isRedsTurn = true;
    private Move move = Move.builder().build();
    private final DrawController drawController;
    private final WinLossController winLossController;
    private final BoardUpdateController boardUpdateController;
    private final GuiMessageController guiMessageController;

    public MasterClientController(Client client) {
        this.client = client;
        this.drawController = new DrawController(this);
        this.winLossController = new WinLossController(this);
        this.boardUpdateController = new BoardUpdateController(this);
        this.guiMessageController = new GuiMessageController(this);
    }

    public void setUp() {
        view.setUp();
    }

    public void processMessageFromServer(ServerMessageToClient serverResponseToClient) {
        ServerToClientMessageType serverResponseType = serverResponseToClient.getServerResponseType();
        switch (serverResponseType) {
            case ASSIGN_WHITE_COLOUR:
                assignColour(Colour.WHITE);
                break;
            case ASSIGN_RED_COLOUR:
                assignColour(Colour.RED);
                break;
            case DECLINE_MOVE:
                guiMessageController.invalidMovePopUpMessage();
                break;
            case INFORM_OF_DRAW_ACCEPTED:
                drawController.drawOfferAcceptedViewUpdate();
                break;
            case INFORM_RED_IS_WINNER:
                winLossController.viewUpdateIfWhiteLost();
                break;
            case INFORM_WHITE_IS_WINNER:
                winLossController.viewUpdateIfRedLost();
                break;
            case UPDATE_BOARD_CHANGE_OF_TURN:
                boardUpdateController.updateBoardChangeOfTurn(serverResponseToClient);
                break;
            case UPDATE_BOARD_SAME_TURN:
                boardUpdateController.updateBoardSameTurn(serverResponseToClient);
                break;
            case INFORM_OTHER_PLAYER_RESIGNED:
                winLossController.otherPlayerResignationActions();
                break;
            case INFORM_DRAW_OFFER_MADE:
                drawController.drawOfferMadeByOtherClientViewUpdate();
                break;
            case INFORM_OTHER_CLIENT_CLOSED_WINDOW:
                otherClientClosedGuiActions();
                break;
            default:
                throw new IllegalArgumentException("Unexpected response type: " + serverResponseType);
        }
    }

    private void otherClientClosedGuiActions() {
        log.warn("Shutting down as your opponent closed their window");
        view.getFrame().dispatchEvent(new WindowEvent(view.getFrame(), WindowEvent.WINDOW_CLOSING));
        client.getClientMessagingService().closeSession();
    }

    void gameOverActions(){
        view.getOfferNewGameButton().setEnabled(true);
        view.getAcceptDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);

        gameInProgress = false;
    }

    public void assignColour(Colour colour) {
        amIRed = colour == Colour.RED;
        gameInProgress = true;
        guiMessageController.bothPlayersReadyMessage();
    }

    public void boardSquareClicked(int column, int row) {
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
        if (move.isEndCoordinatesProvided()) {
            JOptionPane.showMessageDialog(view.getFrame(), "Please wait while your move is processed.");
            return;
        }
        if(move.noStartOrEndSquareProvidedYet()){
            move.setStartCoordinates(column, row);
            return;
        }
        if(move.startCoordinatesOnlyProvided()){
            move.setEndCoordinates(column, row);
            move.setStartAndEndCoordinatesProvided(true);
            client.getClientMessagingService().sendMoveToServer(move);
            move = Move.builder().build();
        }
    }
    public void changeTurns() {
        isRedsTurn = !isRedsTurn;
        guiMessageController.turnOverMessage();
        drawController.withDrawOfferIfPending();
    }

    public void offerNewGameButtonPressed() {
        view.getOfferNewGameButton().setEnabled(false);
        guiMessageController.offerNewGameButtonPressedMessage();

        client.getClientMessagingService().sendOfferNewGameRequest(client.getClientId());
    }


    public void exitDueToThisClientGuiClose() {
        client.getClientMessagingService()
                .tellServerClientExitedThenCloseSession
                        (client.getClientId(), "Exiting as other player has closed their window");
    }
}