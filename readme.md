The example shows how to use Gameplay class to test your bot TestBot.
The Gameplay class contains the game logic, in particular `parseString`, `createMatch`, `step`
and other functions, so the developer can debug the bot logic locally without the server.
```
class TestEnv{
    public static void main(String[] args) {
            Gameplay gameplay = new Gameplay();
            List<Bot> bots = Arrays.asList(new MyBot("1"), new MyBot("2"));
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
}
```