package com.github.jeffw12345.draughts.game.models.move.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpwardOvertakeJumpTest {

    @Test
    public void testRowChange() {
        assertEquals(2, UpwardOvertakeJump.JUMP_TWO_UP_RIGHT.getRowChange());
        assertEquals(2, UpwardOvertakeJump.JUMP_TWO_UP_LEFT.getRowChange());
    }

    @Test
    public void testColumnChange() {
        assertEquals(2, UpwardOvertakeJump.JUMP_TWO_UP_RIGHT.getColumnChange());
        assertEquals(-2, UpwardOvertakeJump.JUMP_TWO_UP_LEFT.getColumnChange());
    }

    @Test
    public void testIsOutOfBoundsForPieceAtPosition() {
        assertFalse(UpwardOvertakeJump.JUMP_TWO_UP_RIGHT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(UpwardOvertakeJump.JUMP_TWO_UP_LEFT.isOutOfBoundsForPieceAtPosition(3, 4));

        assertTrue(UpwardOvertakeJump.JUMP_TWO_UP_RIGHT.isOutOfBoundsForPieceAtPosition(0, 7));
        assertTrue(UpwardOvertakeJump.JUMP_TWO_UP_LEFT.isOutOfBoundsForPieceAtPosition(0, 0));
    }

    @Test
    public void testGetDestinationRowFromStartRow() {
        assertEquals(5, UpwardOvertakeJump.JUMP_TWO_UP_RIGHT.getDestinationRowFromStartRow(3));
        assertEquals(5, UpwardOvertakeJump.JUMP_TWO_UP_LEFT.getDestinationRowFromStartRow(3));
    }

    @Test
    public void testGetDestinationColumnFromStartColumn() {
        assertEquals(6, UpwardOvertakeJump.JUMP_TWO_UP_RIGHT.getDestinationColumnFromStartColumn(4));
        assertEquals(2, UpwardOvertakeJump.JUMP_TWO_UP_LEFT.getDestinationColumnFromStartColumn(4));
    }
}
