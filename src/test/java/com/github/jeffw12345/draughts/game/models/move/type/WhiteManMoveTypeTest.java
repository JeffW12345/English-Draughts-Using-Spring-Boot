package com.github.jeffw12345.draughts.game.models.move.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WhiteManMoveTypeTest {

    @Test
    public void testRowChange() {
        assertEquals(-1, WhiteManMoveType.MOVE_ONE_DOWN_RIGHT.getRowChange());
        assertEquals(-2, WhiteManMoveType.JUMP_TWO_DOWN_RIGHT.getRowChange());
        assertEquals(-1, WhiteManMoveType.MOVE_ONE_DOWN_LEFT.getRowChange());
        assertEquals(-2, WhiteManMoveType.JUMP_TWO_DOWN_LEFT.getRowChange());
    }

    @Test
    public void testColumnChange() {
        assertEquals(1, WhiteManMoveType.MOVE_ONE_DOWN_RIGHT.getColumnChange());
        assertEquals(2, WhiteManMoveType.JUMP_TWO_DOWN_RIGHT.getColumnChange());
        assertEquals(-1, WhiteManMoveType.MOVE_ONE_DOWN_LEFT.getColumnChange());
        assertEquals(-2, WhiteManMoveType.JUMP_TWO_DOWN_LEFT.getColumnChange());
    }

    @Test
    public void testIsOutOfBoundsForPieceAtPosition() {
        assertFalse(WhiteManMoveType.MOVE_ONE_DOWN_RIGHT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(WhiteManMoveType.JUMP_TWO_DOWN_RIGHT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(WhiteManMoveType.MOVE_ONE_DOWN_LEFT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(WhiteManMoveType.JUMP_TWO_DOWN_LEFT.isOutOfBoundsForPieceAtPosition(3, 4));

        assertTrue(WhiteManMoveType.MOVE_ONE_DOWN_RIGHT.isOutOfBoundsForPieceAtPosition(7, 7));
        assertTrue(WhiteManMoveType.JUMP_TWO_DOWN_RIGHT.isOutOfBoundsForPieceAtPosition(7, 7));
        assertTrue(WhiteManMoveType.MOVE_ONE_DOWN_LEFT.isOutOfBoundsForPieceAtPosition(7, 0));
        assertTrue(WhiteManMoveType.JUMP_TWO_DOWN_LEFT.isOutOfBoundsForPieceAtPosition(7, 0));
    }

    @Test
    public void testGetDestinationRowFromStartRow() {
        assertEquals(2, WhiteManMoveType.MOVE_ONE_DOWN_RIGHT.getDestinationRowFromStartRow(3));
        assertEquals(1, WhiteManMoveType.JUMP_TWO_DOWN_RIGHT.getDestinationRowFromStartRow(3));
        assertEquals(2, WhiteManMoveType.MOVE_ONE_DOWN_LEFT.getDestinationRowFromStartRow(3));
        assertEquals(1, WhiteManMoveType.JUMP_TWO_DOWN_LEFT.getDestinationRowFromStartRow(3));
    }

    @Test
    public void testGetDestinationColumnFromStartColumn() {
        assertEquals(5, WhiteManMoveType.MOVE_ONE_DOWN_RIGHT.getDestinationColumnFromStartColumn(4));
        assertEquals(6, WhiteManMoveType.JUMP_TWO_DOWN_RIGHT.getDestinationColumnFromStartColumn(4));
        assertEquals(3, WhiteManMoveType.MOVE_ONE_DOWN_LEFT.getDestinationColumnFromStartColumn(4));
        assertEquals(2, WhiteManMoveType.JUMP_TWO_DOWN_LEFT.getDestinationColumnFromStartColumn(4));
    }
}

