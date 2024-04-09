package com.github.jeffw12345.draughts.game.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Square {
    private SquareContent squareContent;
    public boolean containsAKing(){
        return SquareContent.isAKing(squareContent);
    }
}
