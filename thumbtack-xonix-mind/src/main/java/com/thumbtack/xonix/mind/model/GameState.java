package com.thumbtack.xonix.mind.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.UUID;

@Builder
@Value
@Wither
public class GameState {

    UUID idGame;

    Field field;
}
