package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.Square;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import com.github.jeffw12345.draughts.models.game.move.Move;

public class BoardUpdate {

    public static void updateBoardAfterMove(Game game, Move move, Board board) {
        if (move == null) {
            throw new IllegalArgumentException("Invalid move: null");
            // TODO - Code to exit gracefully
        }

        Square startSquareOnBoard = move.getStartOfMoveSquare(board);
        Square destinationSquare = move.getMoveTerminationSquare(board);
        Square middleSquare = move.getIntermediateSquare(board);
        Colour colourOfPieceBeingMoved = SquareContent.getColour(startSquareOnBoard.getSquareContent());

        startSquareOnBoard.setSquareContent(SquareContent.EMPTY);

        if (move.isOneSquareMove()) {
            oneSquareMoveActions(move, destinationSquare, colourOfPieceBeingMoved, game);
        } else if (move.isOvertakingMove()) {
            twoSquareMoveActions(move, destinationSquare, middleSquare, colourOfPieceBeingMoved, game);
        }
    }

    private static void twoSquareMoveActions(Move move,
                                             Square destinationSquare,
                                             Square middleSquare,
                                             Colour colourOfPieceBeingMoved,
                                             Game game) {
        if (move.willMoveResultInCoronation(game)){
            setSquareContent(destinationSquare, Colour.getKingSquareContent(colourOfPieceBeingMoved));
        }else{
            setSquareContent(destinationSquare, Colour.getManSquareContent(colourOfPieceBeingMoved));
        }
        middleSquare.setSquareContent(SquareContent.EMPTY);
    }

    private static void oneSquareMoveActions(Move move,
                                             Square destinationSquare,
                                             Colour colourOfPieceBeingMoved,
                                             Game game) {
        if (move.willMoveResultInCoronation(game)) {
            setSquareContent(destinationSquare, Colour.getKingSquareContent(colourOfPieceBeingMoved));
        } else {
            setSquareContent(destinationSquare, Colour.getManSquareContent(colourOfPieceBeingMoved));
        }
    }

    private static void setSquareContent(Square square, SquareContent content) {
        square.setSquareContent(content);
    }
}
