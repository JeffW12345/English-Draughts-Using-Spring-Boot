package com.github.jeffw12345.draughts.server.messaging.processing;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.Game;
import com.github.jeffw12345.draughts.game.models.SquareContent;
import com.github.jeffw12345.draughts.game.models.move.Move;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doReturn;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MoveValidationServiceTest {

    private Game game;
    private Move move;
    private Board board;
    private JumpPossibleCheckService jumpService;

    @BeforeEach
    void setUp() {
        game = mock(Game.class);
        move = mock(Move.class);
        board = mock(Board.class);
        jumpService = mock(JumpPossibleCheckService.class);

        when(game.getCurrentBoard()).thenReturn(board);
    }

    @Test
    void testMoveIllegalIfJumpAvailableAndMoveNotOvertaking() {
        when(move.getColourOfPlayerMakingMove()).thenReturn(Colour.WHITE);
        when(move.isOvertakingMove()).thenReturn(false);
        when(jumpService.isJumpPossibleForColour(Colour.WHITE, board)).thenReturn(true);

        assertFalse(MoveValidationService.isMoveLegal(game, move));
    }

    @Test
    void testMoveIllegalIfNotPlayersTurn() {
        when(game.isTurnOfColour(Colour.WHITE)).thenReturn(false);
        when(move.getColourOfPlayerMakingMove()).thenReturn(Colour.WHITE);

        assertFalse(MoveValidationService.isMoveLegal(game, move));
    }

    @Test
    void testMoveIllegalIfStartSquareInvalid() {
        when(move.getColourOfPlayerMakingMove()).thenReturn(Colour.WHITE);
        when(move.getStartSquareRow()).thenReturn(0);
        when(move.getStartSquareColumn()).thenReturn(0);
        when(board.getSquareContentAtRowAndColumn(0, 0)).thenReturn(SquareContent.EMPTY);

        assertFalse(MoveValidationService.isMoveLegal(game, move));
    }

    @Test
    void testMoveIllegalIfEndSquareNotEmpty() {
        when(move.getEndSquareRow()).thenReturn(1);
        when(move.getEndSquareColumn()).thenReturn(1);
        when(board.getSquareContentAtRowAndColumn(1, 1)).thenReturn(SquareContent.RED_MAN);

        assertFalse(MoveValidationService.isMoveLegal(game, move));
    }

    @Test
    void testMoveIllegalIfNotMovingInRightDirection() {
        when(move.getColourOfPlayerMakingMove()).thenReturn(Colour.RED);
        when(move.getStartSquareRow()).thenReturn(2);
        when(move.getStartSquareColumn()).thenReturn(2);
        when(board.getSquareContentAtRowAndColumn(2, 2)).thenReturn(SquareContent.RED_MAN);
        when(move.isMovingUpBoard()).thenReturn(false); // Assuming RED moves up

        assertFalse(MoveValidationService.isMoveLegal(game, move));
    }

    @Test
    void testValidOvertakeLegal() {
        doReturn(Colour.WHITE).when(move).getColourOfPlayerMakingMove();
        doReturn(true).when(game).isTurnOfColour(Colour.WHITE);
        doReturn(5).when(move).getStartSquareRow();
        doReturn(3).when(move).getStartSquareColumn();
        doReturn(SquareContent.WHITE_MAN).when(board).getSquareContentAtRowAndColumn(5, 3);
        doReturn(3).when(move).getEndSquareRow();
        doReturn(5).when(move).getEndSquareColumn();
        doReturn(SquareContent.RED_MAN).when(board).getSquareContentAtRowAndColumn(4, 4);
        doReturn(SquareContent.EMPTY).when(board).getSquareContentAtRowAndColumn(3, 5);
        doReturn(true).when(move).isMovingDownBoard();
        doReturn(true).when(move).isOvertakingMove();

        assertTrue(MoveValidationService.isMoveLegal(game, move));
    }

}
