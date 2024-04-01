package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import com.github.jeffw12345.draughts.models.game.move.KingMoveType;
import com.github.jeffw12345.draughts.models.game.move.MoveType;
import com.github.jeffw12345.draughts.models.game.move.RedManMoveType;
import com.github.jeffw12345.draughts.models.game.move.WhiteManMoveType;

public class PostMoveCheckService {
    public static boolean anyLegalMovesForColour(Board board, Colour colour) {
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
                SquareContent content = board.getSquareContentAtRowAndColumn(rowIndex, columnIndex);

                boolean wrongColour = !content.toString().toLowerCase().contains(colour.toString().toLowerCase());
                if (content == SquareContent.EMPTY || wrongColour) {
                    continue;
                }

                MoveType[] moveTypes = null;
                if (content == SquareContent.WHITE_MAN) {
                    moveTypes = WhiteManMoveType.values();
                } else if (content == SquareContent.RED_MAN) {
                    moveTypes = RedManMoveType.values();
                } else if (content == SquareContent.WHITE_KING || content == SquareContent.RED_KING) {
                    moveTypes = KingMoveType.values();
                }

                if (moveTypes != null) {
                    for (MoveType moveType : moveTypes) {
                        int rowChange = moveType.getRowChange();
                        int columnChange = moveType.getColumnChange();
                        if (!outOfBounds(rowIndex, columnIndex, rowChange, columnChange)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private static boolean outOfBounds(int row, int column, int rowChange, int columnChange){
        row -= rowChange;
        column -= columnChange;

        return row < 0 || row > 7 || column < 0 || column > 7;
    }
}
