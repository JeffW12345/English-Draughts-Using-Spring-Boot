package com.github.jeffw12345.draughts.game.models;

import com.github.jeffw12345.draughts.game.models.move.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testGetSquareContentAtRowAndColumn() {
        assertEquals(SquareContent.RED_MAN, board.getSquareContentAtRowAndColumn(0, 0));

        assertEquals(SquareContent.EMPTY, board.getSquareContentAtRowAndColumn(3, 3));

        assertEquals(SquareContent.WHITE_MAN, board.getSquareContentAtRowAndColumn(7, 1));

        assertThrows(IllegalArgumentException.class, () -> board.getSquareContentAtRowAndColumn(8, 0));
    }


    @Test
    public void testGetSquareAtRowAndColumn() {
        Square square = board.getSquareAtRowAndColumn(0, 0);
        assertNotNull(square);
        assertEquals(SquareContent.RED_MAN, square.getSquareContent());

        assertThrows(IllegalArgumentException.class, () -> board.getSquareAtRowAndColumn(8, 0));
    }

    @Test
    public void testUpdateForCompletedMove() {
        Move move = Move.builder()
                .startSquareColumn(1)
                .startSquareRow(2)
                .startCoordinatesProvided(true)
                .endSquareColumn(2)
                .endSquareRow(3)
                .endCoordinatesProvided(true)
                .colourOfPlayerMakingMove(Colour.RED)
                .build();

        Square startSquare = board.getSquareAtRowAndColumn(move.getStartSquareRow(), move.getStartSquareColumn());
        startSquare.setSquareContent(SquareContent.RED_MAN);

        board.updateForCompletedMove(move);

        assertEquals(SquareContent.EMPTY, startSquare.getSquareContent());
        assertEquals(SquareContent.RED_MAN, board.getSquareContentAtRowAndColumn(move.getEndSquareRow(), move.getEndSquareColumn()));
    }

    @Test
    public void testGetMoveTerminationSquare() {
        Move move = Move.builder()
                .endSquareRow(3)
                .endSquareColumn(4)
                .build();

        Square terminationSquare = board.getMoveTerminationSquare(move);

        assertEquals(SquareContent.EMPTY, terminationSquare.getSquareContent());
    }

    @Test
    public void testHasNoSquaresOfColour() {
        assertTrue(board.hasNoSquaresOfColour(Colour.NONE));

        assertFalse(board.hasNoSquaresOfColour(Colour.RED));
    }

    @Test
    public void testToString() {
        String expectedString =
                "Row number: 7 e w e w e w e w \n" +
                "Row number: 6 w e w e w e w e \n" +
                "Row number: 5 e w e w e w e w \n" +
                "Row number: 4 e e e e e e e e \n" +
                "Row number: 3 e e e e e e e e \n" +
                "Row number: 2 r e r e r e r e \n" +
                "Row number: 1 e r e r e r e r \n" +
                "Row number: 0 r e r e r e r e \n";

        String actualString = board.toString();

        assertEquals(expectedString, actualString);
    }
}