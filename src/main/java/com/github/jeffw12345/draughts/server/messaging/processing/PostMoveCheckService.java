package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.Game;
import com.github.jeffw12345.draughts.game.models.SquareContent;
import com.github.jeffw12345.draughts.game.models.move.Move;
import com.github.jeffw12345.draughts.game.models.move.type.KingMoveType;
import com.github.jeffw12345.draughts.game.models.move.type.MoveType;
import com.github.jeffw12345.draughts.game.models.move.type.RedManMoveType;
import com.github.jeffw12345.draughts.game.models.move.type.WhiteManMoveType;

public class PostMoveCheckService {
    public static boolean isTurnOngoing(Game game, Move move) {
        Board board = game.getCurrentBoard();
        if (move.isOvertakingMove()){
            SquareContent destinationSquareContent = board.getMoveTerminationSquare(move).getSquareContent();
            return isFollowUpOvertakePossible(board, move, destinationSquareContent);
        }
        return false;
    }

    public static boolean isWinForColour(Colour colourOfPlayerWhoseTurnItIs, Board board) {
        Colour otherColour = Colour.getOtherPlayerColour(colourOfPlayerWhoseTurnItIs);
        return noLegalMovesForColour(otherColour, board) || board.hasNoSquaresOfColour(otherColour);
    }

    private static boolean noLegalMovesForColour(Colour colourToCheck, Board board) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                SquareContent content = board.getSquareContentAtRowAndColumn(row, column);
                boolean isSquareOccupiedByPieceOfColour =
                        content != Colour.getManSquareContentForColour(colourToCheck) &&
                        content != Colour.getKingSquareContentForColour(colourToCheck);
                if (!isSquareOccupiedByPieceOfColour) {
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
                        if (!moveType.isOutOfBoundsForPieceAtPosition(row, column)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    static boolean isFollowUpOvertakePossible(Board board, Move move, SquareContent startingSquareContent) {
        if (move == null) {
            throw new IllegalArgumentException("Move parameter cannot be null");
        }

        int endOfMoveRow = move.getEndSquareRow();
        int endOfMoveColumn = move.getEndSquareColumn();
        Colour playerColour = move.getColourOfPlayerMakingMove();

        return new JumpPossibleCheckService().isOvertakePossibleForSquare
                (playerColour, board, startingSquareContent, endOfMoveRow, endOfMoveColumn);
    }
}
