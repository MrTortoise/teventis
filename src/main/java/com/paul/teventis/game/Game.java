package com.paul.teventis.game;

import com.paul.teventis.events.Event;
import com.paul.teventis.events.EventStore;
import com.paul.teventis.set.Set;

public class Game {

    private final EventStore eventStore;
    private final String matchId;
    private TennisScore tennisScore = GameScore.LoveAll;

    public Game(final EventStore eventStore, final String matchId) {
        this.eventStore = eventStore;
        this.matchId = matchId;

        this.eventStore.readAll(streamNameFor(matchId)).forEach(this::when);
        this.eventStore.subscribe(streamNameFor(matchId), this::when);
    }

    public static String streamNameFor(String matchId) {
        return "games-"+matchId;
    }

    public void when(Event e) {
        if (someoneHasWon()) {
            tennisScore = GameScore.LoveAll;
        }

        if (e instanceof PlayerOneScored) {
            tennisScore = tennisScore.when((PlayerOneScored) e);
            checkForGameWon();
        }

        if (e instanceof PlayerTwoScored) {
            tennisScore = tennisScore.when((PlayerTwoScored) e);
            checkForGameWon();
        }
    }

    private void checkForGameWon() {
        if (tennisScore instanceof GamePlayerOne) {
            eventStore.write(Set.streamNameFor(matchId), (GamePlayerOne) tennisScore);
        }

        if (tennisScore instanceof GamePlayerTwo) {
            eventStore.write(Set.streamNameFor(matchId), (GamePlayerTwo) tennisScore);
        }
    }

    private boolean someoneHasWon() {
        return tennisScore instanceof GamePlayerOne
                || tennisScore instanceof GamePlayerTwo;
    }

    public String score() {
        return this.tennisScore.toString();
    }
}
