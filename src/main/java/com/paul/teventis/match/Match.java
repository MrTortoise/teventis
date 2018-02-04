package com.paul.teventis.match;

import com.paul.teventis.events.Event;
import com.paul.teventis.events.EventStore;
import com.paul.teventis.game.Game;

import java.util.List;
import java.util.function.Consumer;

public class Match {

    private final Game game;
    private final Set set;
    private Consumer<String> subscribeToMatchWon;

    public Match(EventStore eventStore, String matchId) {

        final String stream = streamNameFor(matchId);
        eventStore.readAll(stream).forEach(this::when);
        eventStore.subscribe(stream, this::when);

        this.game = new Game();
        this.set = new Set(game);

        this.set.subscribeToSetScores(this::onSetWon);
    }

    private void onSetWon(List<String> setScores) {
        if (setScores.size() >= 3) {
            subscribeToMatchWon.accept("Game, Set, and Match to player one");
        }
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

    public void subscribeToWinningScores(Consumer<List<String>> subscription) {
        this.set.subscribeToSetScores(subscription);
    }

    public void subscribeToMatchWon(Consumer<String> subscription) {
        this.subscribeToMatchWon = subscription;
    }
}
