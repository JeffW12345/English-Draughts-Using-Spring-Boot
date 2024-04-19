package com.github.jeffw12345.draughts.game.models.move.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RedManMoveTypeTest {

    @Test
    public void testRowChange() {
        assertEquals(1, RedManMoveType.MOVE_ONE_UP_RIGHT.getRowChange());
        assertEquals(2, RedManMoveType.JUMP_TWO_UP_RIGHT.getRowChange());
        assertEquals(1, RedManMoveType.MOVE_ONE_UP_LEFT.getRowChange());
        assertEquals(2, RedManMoveType.JUMP_TWO_UP_LEFT.getRowChange());
    }

    @Test
    public void testColumnChange() {
        assertEquals(1, RedManMoveType.MOVE_ONE_UP_RIGHT.getColumnChange());
        assertEquals(2, RedManMoveType.JUMP_TWO_UP_RIGHT.getColumnChange());
        assertEquals(-1, RedManMoveType.MOVE_ONE_UP_LEFT.getColumnChange());
        assertEquals(-2, RedManMoveType.JUMP_TWO_UP_LEFT.getColumnChange());
    }

    @Test
    public void testIsOutOfBoundsForPieceAtPosition() {
        assertFalse(RedManMoveType.MOVE_ONE_UP_RIGHT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(RedManMoveType.JUMP_TWO_UP_RIGHT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(RedManMoveType.MOVE_ONE_UP_LEFT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(RedManMoveType.JUMP_TWO_UP_LEFT.isOutOfBoundsForPieceAtPosition(3, 4));

        assertTrue(RedManMoveType.MOVE_ONE_UP_RIGHT.isOutOfBoundsForPieceAtPosition(0, 7));
        assertTrue(RedManMoveType.JUMP_TWO_UP_RIGHT.isOutOfBoundsForPieceAtPosition(0, 7));
        assertTrue(RedManMoveType.MOVE_ONE_UP_LEFT.isOutOfBoundsForPieceAtPosition(0, 0));
        assertTrue(RedManMoveType.JUMP_TWO_UP_LEFT.isOutOfBoundsForPieceAtPosition(0, 0));
    }

    @Test
    public void testGetDestinationRowFromStartRow() {
        assertEquals(4, RedManMoveType.MOVE_ONE_UP_RIGHT.getDestinationRowFromStartRow(3));
        assertEquals(5, RedManMoveType.JUMP_TWO_UP_RIGHT.getDestinationRowFromStartRow(3));
        assertEquals(4, RedManMoveType.MOVE_ONE_UP_LEFT.getDestinationRowFromStartRow(3));
        assertEquals(5, RedManMoveType.JUMP_TWO_UP_LEFT.getDestinationRowFromStartRow(3));
    }

    @Test
    public void testGetDestinationColumnFromStartColumn() {
        assertEquals(5, RedManMoveType.MOVE_ONE_UP_RIGHT.getDestinationColumnFromStartColumn(4));
        assertEquals(6, RedManMoveType.JUMP_TWO_UP_RIGHT.getDestinationColumnFromStartColumn(4));
        assertEquals(3, RedManMoveType.MOVE_ONE_UP_LEFT.getDestinationColumnFromStartColumn(4));
        assertEquals(2, RedManMoveType.JUMP_TWO_UP_LEFT.getDestinationColumnFromStartColumn(4));
    }
}
