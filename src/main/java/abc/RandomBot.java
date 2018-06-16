package abc;

import com.lineate.xonix.mind.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

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
        this(name, new Random());
    }

    public RandomBot(String name, Random random) {
        this.name = name;
        this.random = random;
    }

    public String getName() {
        return name;
    }

    public Move move(GameState gs) {
        int idx = gs.botId;
        Cell[][] field = gs.cells;
        Point head = gs.me.head().get();
        List<Tail> bodies = gs.others;
        Player me = gs.me;

        if (lastMove != null) {
            Point newHead = calculateHead(field, head, lastMove);
            // don't try to select the last move, if it is to bite itself
            if (me.contains(newHead))
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
                Point p = destination;
                if (Math.abs(p.getRow() - head.getRow()) + Math.abs(p.getCol() - head.getCol()) <= 1)
                    destination = null;
            }
            // now choose the move
            if (destination != null) {
                int ri = destination.getRow() - head.getRow();
                int rj = destination.getCol() - head.getCol();
                int r = random.nextInt(4 + Math.abs(ri) + Math.abs(rj));
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
                Point newHead = calculateHead(field, head, move);
                if (!bodies.get(idx).getIt().contains(newHead))
                    break;
            } else if (lastMove == null) {
                move = Move.values()[random.nextInt(4)];
                Point newHead = calculateHead(field, head, move);
                if (!bodies.get(idx).getIt().contains(newHead))
                    break;
            } else {
                // higher probability to choose the last move
                int r = random.nextInt(16);
                move = (r < 4) ? Move.values()[r] : lastMove;
                Point newHead = calculateHead(field, head, move);
                if (!bodies.get(idx).getIt().contains(newHead))
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

    private Point calculateDestination(Random random, GameState gs , Point head) {
        int m = gs.cells.length;
        int n = gs.cells[0].length;
        // put several random dots into the field, and the first empty point
        // is our destination
        for (int k = 1; k<= 16; k++) {
            int i = random.nextInt(m);
            int j = random.nextInt(n);
            Point p = Point.of(i, j);
            if (gs.cells[p.getRow()][p.getCol()].getCellType() == CellType.EMPTY) {
                if (p != head) {
                    return p;
                }
            }
        }
        // cannot choose the destination
        return null;
    }

    public static void main(String[] args) {
        Gameplay gameplay = new Gameplay();
        List<Bot> bots = Arrays.asList(new RandomBot("1"), new RandomBot("2"));
        List<String> botNames = bots.stream().map(Bot::getName).collect(toList());
        ModelGameState mgs = gameplay.createMatch(10, 20, bots, 100L, 0.9, 0).getGameState();
        for (int it = 0; it < 100; it++) {
            for (int k = 0; k < bots.size(); k++) {
                GameState gs = gameplay.getClientGameState(mgs, k);
                Move move = bots.get(k).move(gs);
                gameplay.step(mgs, k, move);
                System.out.println("move = " + move + " current game state = \n" +
                        gameplay.describeGameState(mgs, botNames, false, false));
            }
        }
    }
}
