package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.Game;
import com.github.jeffw12345.draughts.game.models.SquareContent;
import com.github.jeffw12345.draughts.game.models.move.Move;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PostMoveCheckServiceTest {

    @Test
    public void testIsTurnOngoing_OvertakingMoveFollowUpPossible() {
        Board board = new Board();
        board.setSquareAtRowAndColumn(3, 5, SquareContent.WHITE_MAN);
        board.setSquareAtRowAndColumn(1, 3, SquareContent.EMPTY);
        Move move = new Move();
        move.setEndCoordinates(3, 5);
        move.setColourOfPlayerMakingMove(Colour.WHITE);

        boolean result = PostMoveCheckService.isFollowUpOvertakePossible(board, move, SquareContent.WHITE_MAN);

        assertTrue(result);
    }

    @Test
    public void testIsTurnOngoing_NoOvertakingMove() {
        Game game = mock(Game.class);
        Move move = mock(Move.class);
        when(move.isOvertakingMove()).thenReturn(false);

        assertFalse(PostMoveCheckService.isTurnOngoing(game, move));
    }

    @Test
    public void isWinForColour_whiteToMoveAndNoRedPiecesLeft() {
        Board board = new Board();
        for(int row = 0; row < 8; row++){
            for(int column = 0; column < 8; column++){
                if(board.getSquareContentAtRowAndColumn(row, column) == SquareContent.RED_MAN){
                    board.setSquareAtRowAndColumn(row, column, SquareContent.EMPTY);
                }
            }
        }
        assertTrue(PostMoveCheckService.isWinForColour(Colour.WHITE, board));
    }
}
