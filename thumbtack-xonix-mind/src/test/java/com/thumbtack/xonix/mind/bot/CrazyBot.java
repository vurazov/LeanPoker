package com.thumbtack.xonix.mind.bot;

import com.thumbtack.xonix.mind.model.GameState;
import com.thumbtack.xonix.mind.model.Move;

import java.util.Random;

public class CrazyBot implements Bot {
    private String name;
    private final Random random;
    public CrazyBot(String name, Random random) {
        this.name = name;
        this.random = random;
    }
    @Override
    public String name() {
        return name;
    }
    public Move move(GameState gs) {
        return Move.values()[random.nextInt(4)];
    }
    public void shutdown() { }
}
