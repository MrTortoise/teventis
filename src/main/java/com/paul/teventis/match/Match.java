package com.paul.teventis.match;

import com.paul.teventis.events.Event;
import com.paul.teventis.events.EventStore;
import com.paul.teventis.game.Game;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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

    private void onSetWon(List<SetScore> setScores) {

        final Integer playerOneSetsWon = countPlayersSetsWon(setScores, ss -> ss.getPlayerOneGames() > ss.getPlayerTwoGames());

        final Integer playerTwoSetsWon = countPlayersSetsWon(setScores, ss -> ss.getPlayerTwoGames() > ss.getPlayerOneGames());

        if (playerOneSetsWon >= 3) {
            subscribeToMatchWon.accept("Game, Set, and Match to player one");
        }

        if (playerTwoSetsWon >= 3) {
            subscribeToMatchWon.accept("Game, Set, and Match to player two");
        }
    }

    private int countPlayersSetsWon(List<SetScore> setScores, Function<SetScore, Boolean> didPlayerWinSet) {
        return setScores
                   .stream()
                   .map(didPlayerWinSet)
                   .mapToInt(p -> p ? 1 : 0)
                   .sum();
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

    public void subscribeToWinningScores(Consumer<List<SetScore>> subscription) {
        this.set.subscribeToSetScores(subscription);
    }

    public void subscribeToMatchWon(Consumer<String> subscription) {
        this.subscribeToMatchWon = subscription;
    }
}
