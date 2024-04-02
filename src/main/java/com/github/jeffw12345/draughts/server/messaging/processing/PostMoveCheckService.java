package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.game.move.type.BackwardJump;
import com.github.jeffw12345.draughts.models.game.move.type.ForwardJump;
import com.github.jeffw12345.draughts.models.game.move.type.KingMoveType;
import com.github.jeffw12345.draughts.models.game.move.type.MoveType;
import com.github.jeffw12345.draughts.models.game.move.type.RedManMoveType;
import com.github.jeffw12345.draughts.models.game.move.type.WhiteManMoveType;

public class PostMoveCheckService {

    public static boolean hasColourWon(Board board, Colour colourToCheckForWin){
        Colour colourToCheckForLoss = Colour.otherPlayerColour(colourToCheckForWin);
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            for (int columnIndex = 0; columnIndex < 8; columnIndex++) {
                SquareContent squareContent = board.getSquareContentAtRowAndColumn(rowIndex, columnIndex);
                if(SquareContent.getColour(squareContent) == colourToCheckForLoss){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean wasWinningMove(Game game){
        Colour currentPlayerColour = game.getColourOfPlayerWhoseTurnItIs();
        return hasColourWon(game.getCurrentBoard(), currentPlayerColour);
    }

    public static boolean isStalemate(Game game){
        Colour colourOfPlayerWhoseTurnItIs = game.getColourOfPlayerWhoseTurnItIs();
        return anyLegalMovesForColour(game.getCurrentBoard(), colourOfPlayerWhoseTurnItIs);
    }

    public static boolean isFollowUpOvertakePossible(Board board, SquareContent startingSquareContent, Move move) {
        if (SquareContent.canPieceTypeJumpBothDirections(startingSquareContent)){
            return jumpPossibleForMoveType(board, startingSquareContent, move, ForwardJump.class) ||
                    jumpPossibleForMoveType(board, startingSquareContent, move, BackwardJump.class);
        }
        if (SquareContent.canPieceTypeJumpDownwardsFromTopOnly(startingSquareContent)){
            return jumpPossibleForMoveType(board, startingSquareContent, move, BackwardJump.class);
        }
        if (SquareContent.canPieceTypeJumpUpwardsFromBottomOnly(startingSquareContent)){
            return jumpPossibleForMoveType(board, startingSquareContent, move, ForwardJump.class);
        }
        return false;
    }

    private static boolean jumpPossibleForMoveType(Board board,
                                                   SquareContent startingSquareContent,
                                                   Move move,
                                                   Class<? extends Enum<? extends MoveType>> moveTypeEnumClass) {
        MoveType[] moveTypes = (MoveType[]) moveTypeEnumClass.getEnumConstants();
        for (MoveType moveType : moveTypes) {
            int startRow = move.getEndSquareRow();
            int startColumn = move.getEndSquareColumn();
            int rowChange = moveType.getRowChange();
            int columnChange = moveType.getColumnChange();
            if (!outOfBounds(startRow, startColumn, rowChange, columnChange)) {
                int jumpedSquareRow = rowChange < 0
                        ? startRow + rowChange + 1
                        : startRow + rowChange -1;
                int jumpedSquareColumn = columnChange < 0
                        ? startColumn + columnChange + 1
                        : startColumn + columnChange -1;

                SquareContent middleSquareContent = board
                        .getSquareContentAtRowAndColumn(jumpedSquareRow, jumpedSquareColumn);
                Colour startingSquare = SquareContent.getColour(startingSquareContent);
                Colour opponentColour = Colour.otherPlayerColour(startingSquare);
                if(SquareContent.getColour(middleSquareContent) == opponentColour){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isTurnOngoing(Game game, Move move) {
        Board board = game.getCurrentBoard();
        if (move.isOvertakingMove()){
            SquareContent destinationSquareContent = move.getMoveTerminationSquare(board).getSquareContent();
            return PostMoveCheckService.isFollowUpOvertakePossible(board, destinationSquareContent, move);
        }
        return false;
    }
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
