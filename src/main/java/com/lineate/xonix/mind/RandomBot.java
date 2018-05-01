package com.lineate.xonix.mind;

import com.lineate.xonix.mind.model.Bot;
import com.lineate.xonix.mind.model.Cell;
import com.lineate.xonix.mind.model.CellType;
import com.lineate.xonix.mind.model.GameStateView;
import com.lineate.xonix.mind.model.Move;
import com.lineate.xonix.mind.model.Point;
import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Hello world!
 */
public class RandomBot implements Bot {

    String name;

    int attempts = 8;

    Random random;

    Move lastMove;

    Point destination;

    public RandomBot() {
        this("RandomBot");
    }

    public RandomBot(String name) {
        this(name, new Random(1000));
    }

    public RandomBot(String name, Random random) {
        this.name = name;
        this.random = random;
    }

    public String getName() {
        return name;
    }

    public Move move(GameStateView gs) {
        int id = gs.botId;
        Cell[][] field = gs.field;
        Point head = gs.head;
        Map<Integer, List<Point>> bodies = getBodies(field);

        // TODO this should be in the control loop
        if (!bodies.containsKey(id))
            return Move.STOP; // on the border or filled area

        if (lastMove != null) {
            Point newHead = calculateHead(field, head, lastMove);
            // don't try to select the last move, if it is to bite itself
            if (bodies.get(id).contains(newHead))
                lastMove = null;
        }

        Move move = null;
        // some attempts to move
        for (int i = 0; i < attempts; i++) {
            if (destination == null) {
                destination = calculateDestination(random, gs, head);
            } else {
                // probably reset if we achieved the destination
                // gs.field.cells[head] != Cell.Empty
                val p = destination;
                if (Math.abs(p.getRow() - head.getRow()) + Math.abs(p.getCol() - head.getCol()) <= 1)
                    destination = null;
            }
            // now choose the move
            if (destination != null) {
                val ri = destination.getRow() - head.getRow();
                val rj = destination.getCol() - head.getCol();
                val r = random.nextInt(4 + Math.abs(ri) + Math.abs(rj));
                move = ((Supplier<Move>) () -> {
                    if (r < 4)
                        return Move.values()[r];
                    else if (r < 4 + Math.abs(ri)) {
                        // vertical move
                        return (ri < 0) ? Move.UP : Move.DOWN;
                    } else {
                        // horizontal move
                        return (rj < 0) ? Move.LEFT : Move.RIGHT;
                    }
                }).get();
                val newHead = calculateHead(field, head, move);
                if (!bodies.get(id).contains(newHead))
                    break;
            } else if (lastMove == null) {
                move = Move.values()[random.nextInt(4)];
                val newHead = calculateHead(field, head, move);
                if (!bodies.get(id).contains(newHead))
                    break;
            } else {
                // higher probability to choose the last move
                val r = random.nextInt(16);
                move = (r < 4) ? Move.values()[r] : lastMove;
                val newHead = calculateHead(field, head, move);
                if (!bodies.get(id).contains(newHead))
                    break;
            }
        }

        lastMove = (move == null) ? Move.STOP : move;
        // if after all those attempts we don't found the move, just stay
        return lastMove;
    }

    public Move move1(GameStateView gs) {
        int id = gs.botId;
        Cell[][] field = gs.field;
        Point point = gs.head;
        Map<Integer, List<Point>> bodies = getBodies(field);

        // TODO this should be in the control loop
        if (!bodies.containsKey(id))
            return Move.STOP; // on the border or filled area

        if (lastMove != null) {
            Point newHead = calculateHead(field, point, lastMove);
            // don't try to select the last move, if it is to bite itself
            if (bodies.get(id).contains(newHead))
                lastMove = null;
        }

        Move move = null;
        // some attempts to move
        for (int i = 0; i < attempts; i++) {
            if (lastMove == null) {
                move = Move.values()[random.nextInt(4)];
                val newHead = calculateHead(field, point, move);
                if (!bodies.get(id).contains(newHead))
                    break;
            } else {
                // higher probability to choose the last move
                val r = random.nextInt(16);
                move = (r < 4) ? Move.values()[r] : lastMove;

                val newHead = calculateHead(field, point, move);
                if (!bodies.get(id).contains(newHead))
                    break;
            }
        }

        lastMove = (move == null) ? Move.STOP : move;
        // if after all those attempts we don't found the move, just stay
        return lastMove;
    }

    private Point calculateHead(Cell[][] field, Point point, Move move) {
        int row = point.getRow();
        int col = point.getCol();
        int height = field.length;
        int width = field[0].length;
        switch (move) {
            case RIGHT:
                col += (col + 1 < width) ? 1 : 0;
                break;
            case LEFT:
                col -= (col - 1 < 0) ? 0 : 1;
                break;
            case UP:
                row -= (row - 1 < 0) ? 0 : 1;
                break;
            case DOWN:
                row += (row + 1 < height) ? 1 : 0;
                break;
            case STOP: // stay at position
                break;
        }
        return Point.of(row, col);
    }

    public Map<Integer, List<Point>> getBodies(Cell[][] field) {
        int rows = field.length;
        int cols = field[0].length;
        Map<Integer, List<Point>> bodies = new HashMap<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = field[i][j];
                if (cell.getCellType() == CellType.TAIL) {
                    bodies.putIfAbsent(cell.getBotId(), new ArrayList<Point>())
                            .add(Point.of(i, j));
                }
            }
        }
        return bodies;
    }

    private Point calculateDestination(Random random, GameStateView gs , Point head) {
        val m = gs.field.length;
        val n = gs.field[0].length;
        // put several random dots into the field, and the first empty point
        // is our destination
        for (int k = 1; k<= 16; k++) {
            val i = random.nextInt(m);
            val j = random.nextInt(n);
            val p = Point.of(i, j);
            if (gs.field[p.getRow()][p.getCol()].getCellType() == CellType.EMPTY) {
                if (p != head) {
                    return p;
                }
            }
        }
        // cannot choose the destination
        return null;
    }
}