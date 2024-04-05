package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.Game;
import com.github.jeffw12345.draughts.game.models.Player;
import com.github.jeffw12345.draughts.game.models.move.Move;
import com.github.jeffw12345.draughts.game.models.move.MoveStatus;
import com.github.jeffw12345.draughts.server.ClientsAwaitingAGame;
import com.github.jeffw12345.draughts.server.mapping.ClientIdToGameMapping;
import com.github.jeffw12345.draughts.client.io.ClientMessageToServer;
import com.github.jeffw12345.draughts.client.io.models.ClientToServerMessageType;
import com.github.jeffw12345.draughts.server.messaging.io.ServerMessageComposeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMessageController {
    public static void processClientRequest(ClientMessageToServer clientRequestToServer) {
        ClientToServerMessageType clientToServerRequestType = clientRequestToServer.getRequestType();
        String requestingClientId = clientRequestToServer.getClientId();
        switch (clientToServerRequestType) {
            case WANT_GAME:
                wantGameActions(requestingClientId);
                break;
            case MOVE_REQUEST:
                Move move = clientRequestToServer.getMove();
                Colour playerColour = clientRequestToServer.getColourOfClientPlayer();
                moveRequestActions(requestingClientId, move, playerColour);
                break;
            case DRAW_OFFER:
                ServerMessageComposeService.tellOtherClientDrawAOffered(requestingClientId);
                break;
            case DRAW_ACCEPT:
                ServerMessageComposeService.tellOtherClientDrawAccepted(requestingClientId);
                break;
            case RESIGN:
                ServerMessageComposeService.informOtherClientOfResignation(requestingClientId);
                break;
            case EXITING_DUE_TO_GUI_CLOSE:
                exitDueToGuiCloseActions(requestingClientId);
                break;
            default:
                throw new IllegalArgumentException("Unexpected response type: " + clientToServerRequestType);
        }
    }

    private static void establishConnectionActions(ClientMessageToServer clientRequestToServer) {
        //ClientIdToSessionMapping.add(clientRequestToServer.getClientId(), clientRequestToServer.getSession());
        //TODO - Change or remove
    }
    private static void exitDueToGuiCloseActions(String requestingClientId) {
        ServerMessageComposeService.tellOtherClientAboutShutDown(requestingClientId);
    }
    private static void moveRequestActions(String requestingClientId, Move move, Colour playerColour) {
        Game game = ClientIdToGameMapping.getGameForClientId(requestingClientId);

        game.addMove(move, playerColour);

        boolean isMoveLegal = MoveValidationService.isMoveLegal(game, move);
        if (isMoveLegal){
            legalMoveActions(game, move, requestingClientId, playerColour);
        } else {
            illegalMoveActions(move, requestingClientId);
        }
    }

    private static void legalMoveActions(Game game, Move move, String clientId, Colour playerColour) {
        move.moveProcessedUpdate(MoveStatus.COMPLETE);

        Board board = game.getCurrentBoard();
        board.updateForCompletedMove(move);

        if (PostMoveCheckService.isTurnOngoing(game, move)){
            ServerMessageComposeService.informClientsBoardUpdateTurnOngoing(clientId, board);
        } else {
            boolean hasMoveResultedInWin = PostMoveCheckService.isWinForColour(playerColour, board);
            if (hasMoveResultedInWin){
                ServerMessageComposeService.informClientsOfNewBoardAndThatGameWon(clientId, board, playerColour);
            } else{
                ServerMessageComposeService.informClientsBoardUpdateTurnFinished(clientId, board);
            }
            move.setTurnComplete(true);
        }
    }

    private static void illegalMoveActions(Move move, String clientId) {
        move.moveProcessedUpdate(MoveStatus.ILLEGAL);
        ServerMessageComposeService.informClientThatMoveIllegal(clientId);
    }

    private static void wantGameActions(String requestingClientId) {
        if (ClientsAwaitingAGame.atLeastOnePlayerSeekingGame()){
            String otherClientId = ClientsAwaitingAGame.pop();
            newGameSetup(requestingClientId, otherClientId);
        }
        else{
            ClientsAwaitingAGame.addClientId(requestingClientId);
        }
    }

    private static void newGameSetup(String redPlayerClientId, String whitePlayerClientId) {
        Game game = new Game();

        Player redPlayer = new Player(redPlayerClientId, Colour.RED);
        game.addPlayer(redPlayer);

        Player whitePlayer = new Player(whitePlayerClientId, Colour.WHITE);
        game.addPlayer(whitePlayer);

        ClientIdToGameMapping.assignClientIdToGame(redPlayerClientId, game);
        ClientIdToGameMapping.assignClientIdToGame(whitePlayerClientId, game);

        game.newGamePlayerNotificationActions();
    }
}