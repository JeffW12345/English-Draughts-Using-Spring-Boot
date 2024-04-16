package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.view.DraughtsBoardGui;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.move.Move;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerMessageToClient;
import com.github.jeffw12345.draughts.server.messaging.io.models.ServerToClientMessageType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.JOptionPane;

@Getter
@Slf4j
public class MasterClientController {
    private final Client client;
    private final DraughtsBoardGui view = new DraughtsBoardGui(this);
    private boolean amIRed, gameInProgress, isRedsTurn = true;
    private Move move = new Move();
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

    private Move createMoveForColour(boolean amIRed) {
        Colour playerColour = amIRed ? Colour.RED : Colour.WHITE;
        return Move.builder()
                .colourOfPlayerMakingMove(playerColour)
                .build();
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
            case INFORM_CLIENT_OF_ID:
                String clientId = serverResponseToClient.getClientId();
                updateClientId(clientId);
                break;

            default:
                throw new IllegalArgumentException("Unexpected response type: " + serverResponseType);
        }
    }

    private void updateClientId(String clientId) {
        client.setClientId(clientId);
        view.setUp();
        view.clientIdProvidedActions();
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
        move.setColourOfPlayerMakingMove(colour);
        gameInProgress = true;
        guiMessageController.bothPlayersReadyMessage();
        view.buttonEnablingAtStartOfGame();
    }

    public void boardSquareClicked(int row, int column) {
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
            move.setStartCoordinates(row, column);
            return;
        }
        if(move.startCoordinatesOnlyProvided()){
            move.setEndCoordinates(row, column);
            move.setStartAndEndCoordinatesProvided(true);
            client.getClientOutboundMessagingService().sendMoveToServer(move);
            move = createMoveForColour(amIRed);
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

        client.getClientMessageComposeService().sendOfferNewGameRequest(client.getClientId());
    }
}