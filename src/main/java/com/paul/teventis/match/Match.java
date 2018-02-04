package com.paul.teventis.match;

import com.paul.teventis.events.Event;
import com.paul.teventis.events.EventStore;
import com.paul.teventis.game.Game;

import java.util.function.Consumer;

public class Match {

    private final Game game;

    public Match(EventStore eventStore, String matchId) {

        final String stream = streamNameFor(matchId);
        eventStore.readAll(stream).forEach(this::when);
        eventStore.subscribe(stream, this::when);

        this.game = new Game();
    }

    private void when(Event event) {
        game.when(event);
    }

    public static String streamNameFor(String matchId) {
        return "match-"+matchId;
    }

    public void subscribeToGameScore(Consumer<String> subscription) {
        game.subscribeToScore(subscription);
    }
}
