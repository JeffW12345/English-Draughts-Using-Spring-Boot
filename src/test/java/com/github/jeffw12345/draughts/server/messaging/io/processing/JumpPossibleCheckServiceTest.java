package com.github.jeffw12345.draughts.server.messaging.io.processing;

import com.github.jeffw12345.draughts.game.models.Board;
import com.github.jeffw12345.draughts.game.models.Colour;
import com.github.jeffw12345.draughts.game.models.SquareContent;
import com.github.jeffw12345.draughts.server.messaging.processing.JumpPossibleCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JumpPossibleCheckServiceTest {

    private JumpPossibleCheckService service;
    private Board board;

    @BeforeEach
    void setUp() {
        service = new JumpPossibleCheckService();
        board = new Board();
    }
    @Test
    public void testIsJumpPossibleForColour() {
        assertFalse(service.isJumpPossibleForColour(Colour.RED, board));
        assertFalse(service.isJumpPossibleForColour(Colour.WHITE, board));

        board.setSquareAtRowAndColumn(3, 1, SquareContent.WHITE_MAN);
        assertTrue(service.isJumpPossibleForColour(Colour.RED, board));

        board.setSquareAtRowAndColumn(3, 1, SquareContent.WHITE_KING);
        assertTrue(service.isJumpPossibleForColour(Colour.RED, board));

        board.setSquareAtRowAndColumn(2, 0, SquareContent.RED_KING);
        assertTrue(service.isJumpPossibleForColour(Colour.RED, board));

        board.setSquareAtRowAndColumn(4, 2, SquareContent.RED_MAN);
        assertTrue(service.isJumpPossibleForColour(Colour.WHITE, board));

        board.setSquareAtRowAndColumn(4, 2, SquareContent.RED_KING);
        assertTrue(service.isJumpPossibleForColour(Colour.WHITE, board));

        board.setSquareAtRowAndColumn(5, 1, SquareContent.WHITE_KING);
        assertTrue(service.isJumpPossibleForColour(Colour.RED, board));
    }


    @Test
    void testJumpPossibleWithRegularWhiteManDownwards() {
        board.setSquareAtRowAndColumn(4, 2, SquareContent.RED_MAN);
        assertTrue(service.isOvertakePossibleForSquare(Colour.WHITE, board, SquareContent.WHITE_MAN, 5, 1));
    }

    @Test
    void testJumpPossibleWithWhiteKingUpwards() {
        board.setSquareAtRowAndColumn(2, 0, SquareContent.WHITE_KING);
        board.setSquareAtRowAndColumn(3, 1, SquareContent.RED_MAN);
        assertTrue(service.isOvertakePossibleForSquare(Colour.WHITE, board, SquareContent.WHITE_KING, 2, 0));
    }

    @Test
    void testJumpPossibleWithRedKingDownwards() {
        board.setSquareAtRowAndColumn(5, 1, SquareContent.RED_KING);
        board.setSquareAtRowAndColumn(4, 2, SquareContent.WHITE_MAN);
        assertTrue(service.isOvertakePossibleForSquare(Colour.RED, board, SquareContent.RED_KING, 5, 1));
    }

    @Test
    void testJumpPossibleWithRegularRedManUpwards() {
        board.setSquareAtRowAndColumn(3, 1, SquareContent.WHITE_MAN);
        assertTrue(service.isOvertakePossibleForSquare(Colour.RED, board, SquareContent.RED_MAN, 2, 0));
    }

    @Test
    void testNoJumpPossible() {
        board.setSquareAtRowAndColumn(0, 0, SquareContent.WHITE_MAN);
        board.setSquareAtRowAndColumn(1, 1, SquareContent.EMPTY);
        assertFalse(service.isOvertakePossibleForSquare(Colour.WHITE, board, SquareContent.WHITE_MAN, 0, 0));
    }
}