package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.Game;
import com.github.jeffw12345.draughts.game.models.SquareContent;
import com.github.jeffw12345.draughts.game.models.move.Move;
import com.github.jeffw12345.draughts.game.models.move.type.BackwardJump;
import com.github.jeffw12345.draughts.game.models.move.type.ForwardJump;
import com.github.jeffw12345.draughts.game.models.move.type.KingMoveType;
import com.github.jeffw12345.draughts.game.models.move.type.MoveType;
import com.github.jeffw12345.draughts.game.models.move.type.RedManMoveType;
import com.github.jeffw12345.draughts.game.models.move.type.WhiteManMoveType;

public class PostMoveCheckService {
    public static boolean isFollowUpOvertakePossible(Board board, Move move, SquareContent startingSquareContent) {
        if (SquareContent.canPieceTypeJumpBothDirections(startingSquareContent)) {
            return canJumpFromDestinationSquare(move, ForwardJump.class, board, startingSquareContent) ||
                    canJumpFromDestinationSquare(move, BackwardJump.class, board, startingSquareContent);
        }
        if (SquareContent.canPieceTypeJumpDownwardsFromTopOnly(startingSquareContent)) {
            return canJumpFromDestinationSquare(move, BackwardJump.class, board, startingSquareContent);
        }
        if (SquareContent.canPieceTypeJumpUpwardsFromBottomOnly(startingSquareContent)) {
            return canJumpFromDestinationSquare(move, ForwardJump.class, board, startingSquareContent);
        }
        return false;
    }

    public static boolean canJumpFromDestinationSquare(Move move,
                                                       Class<? extends Enum<? extends MoveType>> moveTypeEnumClass,
                                                       Board board,
                                                       SquareContent startingSquareContent) {
        MoveType[] moveTypes = (MoveType[]) moveTypeEnumClass.getEnumConstants();

        for (MoveType moveType : moveTypes) {
            int endRow = move.getEndSquareRow();
            int endColumn = move.getEndSquareColumn();

            if (moveType.isOutOfBoundsForPieceAtPosition(endRow, endColumn)) {
                continue;
            }

            if (!isOvertakingSquareOccupiedByOpponentPiece(startingSquareContent, move, moveType, board)) {
                continue;
            }

            int destinationRow = moveType.getDestinationRow(endRow);
            int destinationColumn = moveType.getDestinationColumn(endColumn);

            if (board.getSquareContentAtRowAndColumn(destinationRow, destinationColumn) == SquareContent.EMPTY) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOvertakingSquareOccupiedByOpponentPiece(SquareContent startingSquareContent,
                                                                    Move move,
                                                                    MoveType moveType,
                                                                    Board board) {
        int startRow = move.getEndSquareRow();
        int startColumn = move.getEndSquareColumn();
        int rowChange = moveType.getRowChange();
        int columnChange = moveType.getColumnChange();

        int jumpedOverSquareRow = rowChange < 0
                ? startRow + rowChange + 1
                : startRow + rowChange -1;
        int jumpedOverSquareColumn = columnChange < 0
                ? startColumn + columnChange + 1
                : startColumn + columnChange -1;

        SquareContent jumpedOverSquareContent = board.getSquareContentAtRowAndColumn
                (jumpedOverSquareRow, jumpedOverSquareColumn);

        Colour playerColour = SquareContent.getColour(startingSquareContent);
        Colour opponentColour = Colour.getOtherPlayerColour(playerColour);
        return SquareContent.getColour(jumpedOverSquareContent) == opponentColour;
    }

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

    public static boolean noLegalMovesForColour(Colour colourToCheck, Board board) {
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
}
