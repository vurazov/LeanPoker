package com.lineate.xonix.mind.model;

/**
 * Safe to be modified, will be reset before the next move.
 */
public class GameStateView {

    public int botId;

    public Point head;

    public Cell[][] field;

    public GameStateView(int botId, Point head, Cell[][] field) {
        this.field = field;
        this.botId = botId;
        this.head = head;

    }
}
