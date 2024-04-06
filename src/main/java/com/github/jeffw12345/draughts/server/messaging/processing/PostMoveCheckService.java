package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.Game;
import com.github.jeffw12345.draughts.game.models.SquareContent;
import com.github.jeffw12345.draughts.game.models.move.Move;
import com.github.jeffw12345.draughts.game.models.move.type.BackwardJump;
import com.github.jeffw12345.draughts.game.models.move.type.ForwardJump;

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
            SquareContent destinationSquareContent = board.getMoveTerminationSquare(move).getSquareContent();
            return PostMoveCheckService.isFollowUpOvertakePossible(board, destinationSquareContent, move);
        }
        return false;
    }

    public static boolean isWinForColour(Colour colourOfPlayerWhoseTurnItIs, Board board) {
        Colour otherColour = Colour.getOtherPlayerColour(colourOfPlayerWhoseTurnItIs);
        return board.hasNoLegalMovesForColour(otherColour) || board.hasNoSquaresOfColour(otherColour);
    }
}
