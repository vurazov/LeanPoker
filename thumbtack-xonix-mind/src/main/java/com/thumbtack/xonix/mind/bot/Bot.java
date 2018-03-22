package com.thumbtack.xonix.mind.bot;

import com.thumbtack.xonix.mind.model.GameState;
import com.thumbtack.xonix.mind.model.Move;

public interface Bot {
    String name();
    Move move(GameState gs);
    void shutdown();
}
