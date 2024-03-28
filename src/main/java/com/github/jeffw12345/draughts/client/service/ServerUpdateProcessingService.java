package com.github.jeffw12345.draughts.client.service;

import com.github.jeffw12345.draughts.client.controller.ClientController;
import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.response.ServerResponseToClient;
import com.github.jeffw12345.draughts.models.response.ServerResponseType;

public class ServerUpdateProcessingService {
    private final ClientController clientController;
    private String clientId;
    public ServerUpdateProcessingService(ClientController clientController) {
        this.clientController = clientController;
        this.clientId = clientController.getClient().getCLIENT_ID();
    }

    public void processMessage(ServerResponseToClient serverResponseToClient) {
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
        this.clientController.repaintBoard(newBoard);
    }

    private void updateBoardChangeOfTurn(ServerResponseToClient serverResponseObject) {
        Board updatedBoard = serverResponseObject.getGame().getCurrentBoard();
        this.clientController.repaintBoard(updatedBoard);
        this.clientController.changeTurns();
        //this.clientController.getGame().changeTurns();//TODO - Does the Client need to update Game object?
    }

    private void winnerActions(Colour winnerColour) {
        // TODO - Put this on server side. Game game = serverResponseObject.getGame();
        //game.setGameStatus(winnerColour == Colour.RED ? GameStatus.RED_VICTORY : GameStatus.WHITE_VICTORY);
        if(winnerColour == Colour.RED){
            this.clientController.viewUpdateIfWhiteLost();
        } else{
            this.clientController.viewUpdateIfRedLost();
        }
    }
    private void stalemateActions() {
        this.clientController.stalemateViewUpdate();
    }

    private void drawAcceptedActions(ServerResponseToClient serverResponseObject) {
        this.clientController.drawOfferAcceptedViewUpdate();
    }

    private void invalidMoveOptions() {
        // TODO - Move id system?
        this.clientController.invalidMove();
    }

    private void gameStartActions(ServerResponseToClient serverResponseObject) {
        boolean isClientWhitePlayer =
                serverResponseObject
                        .getGame()
                        .isPlayerWhitePlayer(serverResponseObject.getPlayer());

        this.clientController.setAmIRed(!isClientWhitePlayer);
        this.clientController.setNewGameAgreedByPlayers(true);
        //TODO - Do I need to update the game id?
    }

    private void assignPlayerIdActions(ServerResponseToClient serverResponseObject) {

    }

    private void noUpdateActions() {
    }

    private void playerResignationActions(ServerResponseToClient client) {
    }
}
