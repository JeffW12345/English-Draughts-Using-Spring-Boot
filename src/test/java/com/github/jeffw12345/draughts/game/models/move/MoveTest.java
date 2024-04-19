package com.github.jeffw12345.draughts.game.models.move;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {
    @Test
    public void testMoveCreation() {
        Move move = new Move();
        assertNotNull(move);
        assertNotNull(move.getMOVE_ID());
        assertEquals(36, move.getMOVE_ID().length());
        assertFalse(move.isStartCoordinatesProvided());
        assertFalse(move.isEndCoordinatesProvided());
        assertFalse(move.isStartAndEndCoordinatesProvided());
        assertFalse(move.isOvertakingMove());
        assertFalse(move.isOneSquareMove());
        assertEquals(MoveStatus.PENDING, move.getMoveStatus());
        assertNull(move.getMoveProcessedTimestamp());
        assertNull(move.getColourOfPlayerMakingMove());
    }

    @Test
    public void testSetStartCoordinates() {
        Move move = new Move();
        move.setStartCoordinates(3, 4);
        assertTrue(move.isStartCoordinatesProvided());
        assertFalse(move.isEndCoordinatesProvided());
        assertFalse(move.isStartAndEndCoordinatesProvided());
        assertEquals(3, move.getStartSquareRow());
        assertEquals(4, move.getStartSquareColumn());
    }

    @Test
    public void testSetEndCoordinates() {
        Move move = new Move();
        move.setEndCoordinates(5, 6);
        assertFalse(move.isStartCoordinatesProvided());
        assertTrue(move.isEndCoordinatesProvided());
        assertFalse(move.isStartAndEndCoordinatesProvided());
        assertEquals(5, move.getEndSquareRow());
        assertEquals(6, move.getEndSquareColumn());
        assertFalse(move.isOneSquareMove());
    }

    @Test
    public void testMoveStatusUpdate() {
        Move move = new Move();
        move.moveProcessedUpdate(MoveStatus.COMPLETE);
        assertEquals(MoveStatus.COMPLETE, move.getMoveStatus());
        assertNotNull(move.getMoveProcessedTimestamp());
    }

    @Test
    public void testWillMoveResultInCoronation() {
        Move move1 = new Move();
        move1.setStartCoordinates(1, 1);
        move1.setEndCoordinates(0, 0);
        assertTrue(move1.willMoveResultInCoronation());

        Move move2 = new Move();
        move2.setStartCoordinates(1, 1);
        move2.setEndCoordinates(7, 0);
        assertTrue(move2.willMoveResultInCoronation());
    }

    @Test
    public void testIsMovingUpBoard() {
        Move move1 = new Move();
        move1.setStartCoordinates(0, 1);
        move1.setEndCoordinates(1, 0);
        assertTrue(move1.isMovingUpBoard());

        Move move2 = new Move();
        move2.setStartCoordinates(2, 1);
        move2.setEndCoordinates(1, 0);
        assertFalse(move2.isMovingUpBoard());
    }

    @Test
    public void testIsMovingDownBoard() {
        Move move1 = new Move();
        move1.setStartCoordinates(2, 1);
        move1.setEndCoordinates(1, 0);
        assertTrue(move1.isMovingDownBoard());

        Move move2 = new Move();
        move2.setStartCoordinates(5, 1);
        move2.setEndCoordinates(6, 0);
        assertFalse(move2.isMovingDownBoard());
    }

    @Test
    public void testStartCoordinatesOnlyProvided() {
        Move move1 = new Move();
        move1.setStartCoordinates(1, 1);
        assertTrue(move1.startCoordinatesOnlyProvided());

        Move move2 = new Move();
        move2.setEndCoordinates(2, 0);
        assertFalse(move2.startCoordinatesOnlyProvided());
    }

    @Test
    public void testNoStartOrEndSquareProvidedYet() {
        Move move1 = new Move();
        assertTrue(move1.noStartOrEndSquareProvidedYet());

        Move move2 = new Move();
        move2.setStartCoordinates(1, 1);
        assertFalse(move2.noStartOrEndSquareProvidedYet());
    }
}
