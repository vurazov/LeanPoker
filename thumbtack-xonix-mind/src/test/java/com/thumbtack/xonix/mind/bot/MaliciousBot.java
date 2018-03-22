package com.thumbtack.xonix.mind.bot;

import com.thumbtack.xonix.mind.model.GameState;
import com.thumbtack.xonix.mind.model.Move;
import com.thumbtack.xonix.mind.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MaliciousBot implements Bot {
    final ExecutorService es = Executors.newFixedThreadPool(9999);
    @Override
    public String name() {
        return "ha-ha";
    }

    @Override
    public Move move(GameState gs) {
        // Utils.sleep(900);
        es.submit(() -> {
            while (true) {
                Utils.sleep(0);
            }
        });
        return Move.LEFT;
    }

    @Override
    public void shutdown() { }
}
