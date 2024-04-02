package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.models.game.Board;
import com.github.jeffw12345.draughts.models.game.Colour;
import com.github.jeffw12345.draughts.models.game.Game;
import com.github.jeffw12345.draughts.models.game.SquareContent;
import com.github.jeffw12345.draughts.models.game.move.Move;
import com.github.jeffw12345.draughts.models.game.move.type.BackwardJump;
import com.github.jeffw12345.draughts.models.game.move.type.ForwardJump;

public class PostMoveCheckService {
    public static boolean isFollowUpOvertakePossible(Board board, SquareContent startingSquareContent, Move move) {
        if (SquareContent.canPieceTypeJumpBothDirections(startingSquareContent)){
            return board.jumpPossibleForMoveType(startingSquareContent, move, ForwardJump.class) ||
                    board.jumpPossibleForMoveType(startingSquareContent, move, BackwardJump.class);
        }
        if (SquareContent.canPieceTypeJumpDownwardsFromTopOnly(startingSquareContent)){
            return board.jumpPossibleForMoveType(startingSquareContent, move, BackwardJump.class);
        }
        if (SquareContent.canPieceTypeJumpUpwardsFromBottomOnly(startingSquareContent)){
            return board.jumpPossibleForMoveType(startingSquareContent, move, ForwardJump.class);
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

    public static boolean isWinForColour(Colour colourOfPlayerWhoseTurnItIs, Board board) {
        Colour otherColour = Colour.otherPlayerColour(colourOfPlayerWhoseTurnItIs);
        return board.hasNoLegalMovesForColour(otherColour) || board.hasNoSquaresOfColour(otherColour);
    }
}
