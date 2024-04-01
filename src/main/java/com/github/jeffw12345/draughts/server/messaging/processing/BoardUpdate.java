package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.Square;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import com.github.jeffw12345.draughts.models.game.move.Move;

public class BoardUpdate {

    public static void updateBoardAfterMove(Move move, Board board){
        int startRow = move.getStartSquare().getRowNumber();
        int startColumn = move.getStartSquare().getColumnNumber();

        Square startSquareOnBoard = board.getSquareAtRowAndColumn(startRow, startColumn);
        SquareContent pieceTypeBeingMoved = startSquareOnBoard.getSquareContent();
        startSquareOnBoard.setSquareContent(SquareContent.EMPTY);

        if (move.isOneSquareMove() && move.willMoveResultInCoronation()){
            Colour colour = SquareContent.getColour(pieceTypeBeingMoved);
            //TODO - Complete
        }
    }
}
