package com.thumbtack.xonix.mind.service;

import com.thumbtack.xonix.mind.bot.Bot;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ServiceBotProvider implements Provider<List<Optional<Bot>>> {

    @Override
    public List<Optional<Bot>> get() {
        return Arrays.asList(Optional.empty());
    }
}
