package com.thumbtack.xonix.mind.bot;

import com.thumbtack.xonix.mind.model.GameState;
import com.thumbtack.xonix.mind.model.Move;
import com.thumbtack.xonix.mind.utils.Utils;

import java.util.Random;

public class GlitchBot implements Bot {
    private final String name;
    private final Random random;
    public GlitchBot(String name, Random random) {
        this.name = name;
        this.random = random;
    }
    public String name() {
        return name;
    }
    public Move move(GameState gs) {
        if (random.nextFloat() < 0.2)
            throw new RuntimeException("Ooops!");
        else {
            // random.nextInt(2) >= 0 is always true
            while (random.nextInt(2) >= 0) {
                Utils.sleep(0);
            }
        }
        return Move.LEFT;
    }
    public void shutdown() { }
}
