package com.github.jeffw12345.draughts.models.game;

public class Board {
    Row[] listOfRows = new Row[8];

    private final Square EMPTY_SQUARE = Square.builder()
            .squareContent(SquareContent.EMPTY)
            .build();

    private final Square WHITE_MAN = Square.builder()
            .squareContent(SquareContent.WHITE_MAN)
            .build();

    private final Square RED_MAN = Square.builder()
            .squareContent(SquareContent.RED_MAN)
            .build();

    public void initialBoard() {
        Row firstRow = createFirstRow();
        Row secondRow = createSecondRow();
        Row thirdRow = createFirstRow();
        Row fourthRow = createEmptyRow();
        Row fifthRow = createEmptyRow();
        Row sixthRow = createSixthRow();
        Row seventhRow = createSeventhRow();
        Row eigthRow = createSixthRow();

        listOfRows[0] = firstRow;
        listOfRows[1] = secondRow;
        listOfRows[2] = thirdRow;
        listOfRows[3] = fourthRow;
        listOfRows[4] = fifthRow;
        listOfRows[5] = sixthRow;
        listOfRows[6] = seventhRow;
        listOfRows[7] = eigthRow;
    }

    private Row createSeventhRow() {
        Row row = new Row();
        for(int column = 0; column < 8; column++){
            if(column % 2 == 0){
                row.getSquaresOnRow()[column] = RED_MAN;
            }else{
                row.getSquaresOnRow()[column] = EMPTY_SQUARE;
            }
        }
        return row;
    }

    private Row createSixthRow() {
        Row row = new Row();
        for(int column = 0; column < 8; column++){
            if(column % 2 != 0){
                row.getSquaresOnRow()[column] = RED_MAN;
            }else{
                row.getSquaresOnRow()[column] = EMPTY_SQUARE;
            }
        }
        return row;
    }

    private Row createEmptyRow() {
        Row row = new Row();
        for(int column = 0; column < 8; column++){
            row.getSquaresOnRow()[column] = EMPTY_SQUARE;
        }
        return row;
    }

    private Row createSecondRow() {
        Row row = new Row();
        for(int column = 0; column < 8; column++){
            if(column % 2 != 0){
                row.getSquaresOnRow()[column] = WHITE_MAN;
            }else{
                row.getSquaresOnRow()[column] = EMPTY_SQUARE;
            }
        }
        return row;
    }

    private Row createFirstRow() {
        Row row = new Row();
        for(int column = 0; column < 8; column++){
            if(column % 2 == 0){
                row.getSquaresOnRow()[column] = WHITE_MAN;
            }else{
                row.getSquaresOnRow()[column] = EMPTY_SQUARE;
            }
        }
        return row;
    }

    public SquareContent getSquareContentAtRowAndColumn(int rowNumber, int columnNumber){
        return listOfRows[rowNumber].getSquareAtColumn(columnNumber).getSquareContent();
    }
}
