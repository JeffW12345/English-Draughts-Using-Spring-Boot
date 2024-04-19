package com.github.jeffw12345.draughts.game.models.move.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KingMoveTypeTest {

    @Test
    public void testRowChange() {
        assertEquals(1, KingMoveType.MOVE_ONE_UP_RIGHT.getRowChange());
        assertEquals(2, KingMoveType.JUMP_TWO_UP_RIGHT.getRowChange());
        assertEquals(1, KingMoveType.MOVE_ONE_UP_LEFT.getRowChange());
        assertEquals(2, KingMoveType.JUMP_TWO_UP_LEFT.getRowChange());
        assertEquals(-1, KingMoveType.MOVE_ONE_DOWN_RIGHT.getRowChange());
        assertEquals(-2, KingMoveType.JUMP_TWO_DOWN_RIGHT.getRowChange());
        assertEquals(-1, KingMoveType.MOVE_ONE_DOWN_LEFT.getRowChange());
        assertEquals(-2, KingMoveType.JUMP_TWO_DOWN_LEFT.getRowChange());
    }

    @Test
    public void testColumnChange() {
        assertEquals(1, KingMoveType.MOVE_ONE_UP_RIGHT.getColumnChange());
        assertEquals(2, KingMoveType.JUMP_TWO_UP_RIGHT.getColumnChange());
        assertEquals(-1, KingMoveType.MOVE_ONE_UP_LEFT.getColumnChange());
        assertEquals(-2, KingMoveType.JUMP_TWO_UP_LEFT.getColumnChange());
        assertEquals(1, KingMoveType.MOVE_ONE_DOWN_RIGHT.getColumnChange());
        assertEquals(2, KingMoveType.JUMP_TWO_DOWN_RIGHT.getColumnChange());
        assertEquals(-1, KingMoveType.MOVE_ONE_DOWN_LEFT.getColumnChange());
        assertEquals(-2, KingMoveType.JUMP_TWO_DOWN_LEFT.getColumnChange());
    }

    @Test
    public void testIsOutOfBoundsForPieceAtPosition() {
        assertFalse(KingMoveType.MOVE_ONE_UP_RIGHT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(KingMoveType.JUMP_TWO_UP_RIGHT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(KingMoveType.MOVE_ONE_UP_LEFT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(KingMoveType.JUMP_TWO_UP_LEFT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(KingMoveType.MOVE_ONE_DOWN_RIGHT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(KingMoveType.JUMP_TWO_DOWN_RIGHT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(KingMoveType.MOVE_ONE_DOWN_LEFT.isOutOfBoundsForPieceAtPosition(3, 4));
        assertFalse(KingMoveType.JUMP_TWO_DOWN_LEFT.isOutOfBoundsForPieceAtPosition(3, 4));

        assertTrue(KingMoveType.MOVE_ONE_UP_RIGHT.isOutOfBoundsForPieceAtPosition(0, 7));
        assertTrue(KingMoveType.JUMP_TWO_UP_RIGHT.isOutOfBoundsForPieceAtPosition(0, 7));
        assertTrue(KingMoveType.MOVE_ONE_UP_LEFT.isOutOfBoundsForPieceAtPosition(0, 0));
        assertTrue(KingMoveType.JUMP_TWO_UP_LEFT.isOutOfBoundsForPieceAtPosition(0, 0));
        assertTrue(KingMoveType.MOVE_ONE_DOWN_RIGHT.isOutOfBoundsForPieceAtPosition(7, 7));
        assertTrue(KingMoveType.JUMP_TWO_DOWN_RIGHT.isOutOfBoundsForPieceAtPosition(7, 7));
        assertTrue(KingMoveType.MOVE_ONE_DOWN_LEFT.isOutOfBoundsForPieceAtPosition(7, 0));
        assertTrue(KingMoveType.JUMP_TWO_DOWN_LEFT.isOutOfBoundsForPieceAtPosition(7, 0));
    }

    @Test
    public void testGetDestinationRowFromStartRow() {
        assertEquals(4, KingMoveType.MOVE_ONE_UP_RIGHT.getDestinationRowFromStartRow(3));
        assertEquals(5, KingMoveType.JUMP_TWO_UP_RIGHT.getDestinationRowFromStartRow(3));
        assertEquals(4, KingMoveType.MOVE_ONE_UP_LEFT.getDestinationRowFromStartRow(3));
        assertEquals(5, KingMoveType.JUMP_TWO_UP_LEFT.getDestinationRowFromStartRow(3));
        assertEquals(2, KingMoveType.MOVE_ONE_DOWN_RIGHT.getDestinationRowFromStartRow(3));
        assertEquals(1, KingMoveType.JUMP_TWO_DOWN_RIGHT.getDestinationRowFromStartRow(3));
        assertEquals(2, KingMoveType.MOVE_ONE_DOWN_LEFT.getDestinationRowFromStartRow(3));
        assertEquals(1, KingMoveType.JUMP_TWO_DOWN_LEFT.getDestinationRowFromStartRow(3));
    }

    @Test
    public void testGetDestinationColumnFromStartColumn() {
        assertEquals(5, KingMoveType.MOVE_ONE_UP_RIGHT.getDestinationColumnFromStartColumn(4));
        assertEquals(6, KingMoveType.JUMP_TWO_UP_RIGHT.getDestinationColumnFromStartColumn(4));
        assertEquals(3, KingMoveType.MOVE_ONE_UP_LEFT.getDestinationColumnFromStartColumn(4));
        assertEquals(2, KingMoveType.JUMP_TWO_UP_LEFT.getDestinationColumnFromStartColumn(4));
        assertEquals(5, KingMoveType.MOVE_ONE_DOWN_RIGHT.getDestinationColumnFromStartColumn(4));
        assertEquals(6, KingMoveType.JUMP_TWO_DOWN_RIGHT.getDestinationColumnFromStartColumn(4));
        assertEquals(3, KingMoveType.MOVE_ONE_DOWN_LEFT.getDestinationColumnFromStartColumn(4));
        assertEquals(2, KingMoveType.JUMP_TWO_DOWN_LEFT.getDestinationColumnFromStartColumn(4));
    }
}
