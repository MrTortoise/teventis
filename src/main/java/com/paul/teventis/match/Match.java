package com.paul.teventis.match;

import com.paul.teventis.events.Event;
import com.paul.teventis.events.EventStore;
import com.paul.teventis.set.SetPlayerOne;
import com.paul.teventis.set.SetPlayerTwo;

public class Match {

    private final EventStore eventStore;
    private final String matchId;
    private int playerOneSetsWon = 0;
    private int playerTwoSetsWon = 0;

    public Match(EventStore eventStore, String matchId) {
        this.eventStore = eventStore;
        this.matchId = matchId;

        final String stream = streamNameFor(matchId);
        this.eventStore.readAll(stream).forEach(this::when);
        this.eventStore.subscribe(stream, this::when);
    }

    private void when(Event event) {
        if (event instanceof SetPlayerOne) {
            this.playerOneSetsWon++;

            if (playerOneIsWinning())
                eventStore.write(streamNameFor(matchId), new MatchPlayerOne());
        }

        if (event instanceof SetPlayerTwo) {
            this.playerTwoSetsWon++;

            if (playerTwoIsWinning())
                eventStore.write(streamNameFor(matchId), new MatchPlayerTwo());
        }
    }

    private boolean playerTwoIsWinning() {
        return playerTwoSetsWon > 2 && atLeastBestOfThree();
    }

    private boolean playerOneIsWinning() {
        return playerOneSetsWon > 2 && atLeastBestOfThree();
    }

    private boolean atLeastBestOfThree() {
        return Math.abs(playerOneSetsWon - playerTwoSetsWon) >= 2;
    }

    public static String streamNameFor(String matchId) {
        return "match-"+matchId;
    }
}
