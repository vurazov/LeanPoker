package com.thumbtack.xonix.mind;

import com.thumbtack.xonix.mind.bot.Bot;
import com.thumbtack.xonix.mind.model.GameState;
import com.thumbtack.xonix.mind.model.Match;
import com.thumbtack.xonix.mind.model.Move;
import com.thumbtack.xonix.mind.service.GamePlayService;
import com.thumbtack.xonix.mind.service.ServiceBotProvider;
import com.thumbtack.xonix.mind.utils.Utils;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
@Slf4j
@SpringBootApplication(scanBasePackages = " com.thumbtack.xonix.mind")
public class Application implements CommandLineRunner {


    final ExecutorService executor = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors());

    @Autowired
    ServiceBotProvider botProvider;

    @Autowired
    GamePlayService gamePlayService;

    public static void main( String[] args )
    {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            List<Bot> bots =  botProvider.get().stream()
                    .filter(Optional::isPresent).map(Optional::get)
                    .collect(Collectors.toList());
            if (bots.isEmpty()){
                log.warn("The bots has no for competition!");
                return;
            }

            //TODO: Implement prosess several matches
            //TODO: User creates match with button Start
            val winner = runMatch(gamePlayService.createMatch(bots));
            log.info("The winner is = " + winner.map(Bot::name));
            // try to shutdown gracefully

        }finally {
            executor.shutdown();
            Utils.sleep(2000);
            if (!executor.isTerminated()) {
                System.out.println("Force to shutdown the executor");
                executor.shutdownNow();
            }
        }
    }

    // Constraints:
    // the bot ate itself
    // the block the start of bot if it play off the previous match
    // the bot play off the match was attacked
    // the start of bot after lose the match should be blocked
    // the bot that has the largest painted area in total is won
    private Optional<Bot> runMatch(Match match) {
        List<Bot> bots = new ArrayList<>(match.getBots());
        Set<Bot> failed = new HashSet<>();
        GameState gs = match.getGameState();
        while (!Thread.interrupted()) {
            if (bots.size() <= 1) {
                // match has ended, 1 or 0 bots are left
                return bots.isEmpty() ? Optional.empty() : Optional.of(bots.get(0));
            } else {
                for (val bot : bots) {
                    val opt = safeCall(bot, gs);
                    System.out.println("bot = " + bot.name() + " moveOpt = " + opt);
                    if (opt.isPresent()) {
                        gs = gameStep(gs, bot, opt.get());
                    } else {
                        // the current bot execution has failed
                        // remove it from the match (how to do it?????)
                        failed.add(bot);
                        gs = gameStep(gs, bot, Move.SUICIDE);
                    }
                }
                if (!failed.isEmpty()) {
                    bots.removeAll(failed);
                    failed.clear();
                }
            }
        }
        return Optional.empty();
    }

    public GameState gameStep(GameState gs, Bot b, Move d) {
        return gs; // just stub
    }

    public Optional<Move> safeCall(Bot bot, GameState gs) {
        val future = executor.submit(() -> bot.move(gs));
        try {
            return Optional.of(future.get(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            // we get here when bot throws an exception or thinks too long
            System.out.println("e = " + e);
            future.cancel(true);
        }
        // empty shows, that the bot does something bad
        return Optional.empty();
    }

}

