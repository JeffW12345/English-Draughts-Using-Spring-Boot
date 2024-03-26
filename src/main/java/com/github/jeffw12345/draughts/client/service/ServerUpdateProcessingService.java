package com.github.jeffw12345.draughts.client.service;

import com.github.jeffw12345.draughts.client.controller.ClientController;
import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.response.ServerResponseToClient;
import com.github.jeffw12345.draughts.models.response.ServerResponseType;

public class ServerUpdateProcessingService {
    private final ClientController clientController;
    public ServerUpdateProcessingService(ClientController clientController) {
        this.clientController = clientController;
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
                assignPlayerColourAndGameId(serverResponseToClient);
                break;
            case DECLINE_MOVE:
                invalidMoveOptions(serverResponseToClient);
                break;
            case INFORM_OF_DRAW_ACCEPTED:
                drawAcceptedActions(serverResponseToClient);
                break;
            case INFORM_OF_STALEMATE:
                stalemateActions(serverResponseToClient);
                break;
            case INFORM_OF_WINNER:
                winnerActions(serverResponseToClient);
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
        this.clientController.updateBoard(newBoard);
    }

    private void updateBoardChangeOfTurn(ServerResponseToClient serverResponseObject) {
        Board newBoard = serverResponseObject.getGame().getCurrentBoard();
        this.clientController.updateBoard(newBoard);
        this.clientController.getGame().changeTurns();
    }

    private void winnerActions(ServerResponseToClient serverResponseObject) {

    }
    private void stalemateActions(ServerResponseToClient serverResponseObject) {
    }

    private void drawAcceptedActions(ServerResponseToClient serverResponseObject) {
    }

    private void invalidMoveOptions(ServerResponseToClient serverResponseObject) {
    }

    private void assignPlayerColourAndGameId(ServerResponseToClient serverResponseObject) {
    }

    private void assignPlayerIdActions(ServerResponseToClient serverResponseObject) {
    }

    private void noUpdateActions() {
    }

    private void playerResignationActions(ServerResponseToClient client) {
    }
}
