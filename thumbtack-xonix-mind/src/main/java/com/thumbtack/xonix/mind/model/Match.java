package com.thumbtack.xonix.mind.model;

import com.thumbtack.xonix.mind.bot.Bot;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.List;

@Builder
@Value
@Wither
public class Match {
    GameState gameState;
    List<Bot> bots;
}
