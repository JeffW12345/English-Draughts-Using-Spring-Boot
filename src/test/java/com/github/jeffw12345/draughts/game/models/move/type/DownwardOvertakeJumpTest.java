package com.github.jeffw12345.draughts.game.models.move.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DownwardOvertakeJumpTest {

    @Test
    public void testRowChange() {
        assertEquals(-2, DownwardOvertakeJump.JUMP_TWO_DOWN_RIGHT.getRowChange());
        assertEquals(-2, DownwardOvertakeJump.JUMP_TWO_DOWN_LEFT.getRowChange());
    }

    @Test
    public void testColumnChange() {
        assertEquals(2, DownwardOvertakeJump.JUMP_TWO_DOWN_RIGHT.getColumnChange());
        assertEquals(-2, DownwardOvertakeJump.JUMP_TWO_DOWN_LEFT.getColumnChange());
    }

    @Test
    public void testIsOutOfBoundsForPieceAtPosition() {
        assertFalse(DownwardOvertakeJump.JUMP_TWO_DOWN_RIGHT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(DownwardOvertakeJump.JUMP_TWO_DOWN_LEFT.isOutOfBoundsForPieceAtPosition(3, 4));

        assertTrue(DownwardOvertakeJump.JUMP_TWO_DOWN_RIGHT.isOutOfBoundsForPieceAtPosition(0, 7));
        assertTrue(DownwardOvertakeJump.JUMP_TWO_DOWN_LEFT.isOutOfBoundsForPieceAtPosition(0, 0));
    }

    @Test
    public void testGetDestinationRowFromStartRow() {
        assertEquals(1, DownwardOvertakeJump.JUMP_TWO_DOWN_RIGHT.getDestinationRowFromStartRow(3));
        assertEquals(1, DownwardOvertakeJump.JUMP_TWO_DOWN_LEFT.getDestinationRowFromStartRow(3));
    }

    @Test
    public void testGetDestinationColumnFromStartColumn() {
        assertEquals(6, DownwardOvertakeJump.JUMP_TWO_DOWN_RIGHT.getDestinationColumnFromStartColumn(4));
        assertEquals(2, DownwardOvertakeJump.JUMP_TWO_DOWN_LEFT.getDestinationColumnFromStartColumn(4));
    }
}