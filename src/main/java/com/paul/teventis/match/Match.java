package com.paul.teventis.match;

import com.paul.teventis.events.Event;
import com.paul.teventis.events.EventStore;

import java.util.function.Consumer;

public class Match {

    private Consumer<String> subscription;

    public Match(EventStore eventStore, String matchId) {

        final String stream = streamNameFor(matchId);
        eventStore.readAll(stream).forEach(this::when);
        eventStore.subscribe(stream, this::when);
    }

    private void when(Event event) {

    }

    public static String streamNameFor(String matchId) {
        return "match-"+matchId;
    }

    public void subscribeToGameScore(Consumer<String> subscription) {
        this.subscription = subscription;
    }
}
