package com.thumbtack.xonix.mind.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Builder
@Value
@Wither
public class Field {
    //TODO simplest implementation
    int[][] cells;
}
