package com.github.jeffw12345.draughts.client.controller;

import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.client.view.DraughtsBoardGui;
import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.messaging.ServerMessageToClient;
import com.github.jeffw12345.draughts.models.messaging.ServerToClientMessageType;
import lombok.Getter;

import javax.swing.JOptionPane;

@Getter //TODO - Check encapsulation as tight as possible.
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
            case INFORM_OF_STALEMATE:
                drawController.stalemateActions();
                break;
            case INFORM_RED_IS_WINNER:
                winLossController.winnerActions(Colour.RED);
                break;
            case INFORM_WHITE_IS_WINNER:
                winLossController.winnerActions(Colour.WHITE);
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
                drawController.ifDrawOfferMadeByOtherClient();
                break;
            default:
                throw new IllegalArgumentException("Unexpected response type: " + serverResponseType);
        }
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
        drawController.drawOfferExpiryActions();
    }

    public void offerNewGameButtonPressed() {
        view.getOfferNewGameButton().setEnabled(false);
        guiMessageController.offerNewGameButtonPressedMessage();

        client.getClientMessagingService().sendOfferNewGameRequest(client.getClientId());
    }

    public void acceptDrawButtonPressed() {
        view.getAcceptDrawButton().setEnabled(false);
        view.getOfferNewGameButton().setEnabled(true);
        gameInProgress = false;
        guiMessageController.acceptDrawButtonPressedMessage();

        client.getClientMessagingService().sendDrawOfferAcceptance(client.getClientId());
    }

    public void offerDrawButtonPressed() {
        view.getOfferDrawButton().setEnabled(false);
        drawController.drawOfferSentPending = true;
        guiMessageController.offerDrawButtonPressedMessage();
        client.getClientMessagingService().sendDrawOfferProposal(client.getClientId());
    }

    public void resignButtonPressed() {
        view.getOfferNewGameButton().setEnabled(true);
        view.getAcceptDrawButton().setEnabled(false);
        view.getResignButton().setEnabled(false);
        view.getOfferDrawButton().setEnabled(false);
        gameInProgress = false;
        guiMessageController.resignButtonPressedMessage();

        client.getClientMessagingService().sendResignation(client.getClientId());
    }

    public void setWelcomeMessage() {
        view.setTopLineMessageText("Welcome to English Draughts!");
        view.setMiddleLineMessageText("");
        view.setBottomLineMessageText(colourMessage());
    }

    public String colourMessage() {
        if (amIRed) {
            return "You are the red player";
        } else {
            return "You are the white player";
        }
    }

    public void exitDueToGuiClose() {

        client.getClientMessagingService().tellServerExited(client.getClientId(), "Exiting as a player has closed their window");
    }
}