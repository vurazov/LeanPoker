package com.thumbtack.xonix.mind.service;

import com.thumbtack.xonix.mind.bot.Bot;
import com.thumbtack.xonix.mind.model.Field;
import com.thumbtack.xonix.mind.model.GameState;
import com.thumbtack.xonix.mind.model.Match;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
public class GamePlayService {

    public Match createMatch(List<Bot> bots){
        val gs = GameState.builder()
                .idGame(UUID.randomUUID())
                .field(Field.builder().build())
                .build();
        return Match.builder().gameState(gs).bots(bots).build();
    }

}
