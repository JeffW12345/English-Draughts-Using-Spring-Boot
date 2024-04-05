package com.github.jeffw12345.draughts.game.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Square {
    private SquareContent squareContent;
}
