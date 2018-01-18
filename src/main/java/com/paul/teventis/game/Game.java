package com.paul.teventis.game;

import com.paul.teventis.events.Event;
import com.paul.teventis.events.EventStore;
import com.paul.teventis.set.Set;

public class Game {

    private final EventStore eventStore;
    private final String matchId;
    private TennisScore tennisScore = new LoveAll();

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
            tennisScore = new LoveAll();
        }

        if (PlayerOneScored.class.isInstance(e)) {
            tennisScore = tennisScore.when((PlayerOneScored) e);
            checkForGameWon();
        } else if (PlayerTwoScored.class.isInstance(e)) {
            tennisScore = tennisScore.when((PlayerTwoScored) e);
            checkForGameWon();
        }
    }

    private void checkForGameWon() {
        if (GamePlayerOne.class.isInstance(tennisScore)) {
            eventStore.write(Set.streamNameFor(matchId), (GamePlayerOne) tennisScore);
        }

        if (GamePlayerTwo.class.isInstance(tennisScore)) {
            eventStore.write(Set.streamNameFor(matchId), (GamePlayerTwo) tennisScore);
        }
    }

    private boolean someoneHasWon() {
        return GamePlayerOne.class.isInstance(tennisScore)
                || GamePlayerTwo.class.isInstance(tennisScore);
    }

    public String score() {
        return this.tennisScore.toString();
    }
}
