package com.paul.teventis.set;

import com.paul.teventis.events.*;
import com.paul.teventis.game.*;
import com.paul.teventis.match.Match;

public class Set {

    private final EventStore eventStore;
    private final String matchId;

    private int gamesPlayerOne = 0;
    private int gamesPlayerTwo = 0;
    private String score = "0-0";

    public Set(final EventStore eventStore, String matchId) {
        this.eventStore = eventStore;
        this.matchId = matchId;

        final String stream = streamNameFor(matchId);
        this.eventStore.readAll(stream).forEach(this::when);
        this.eventStore.subscribe(stream, this::when);
    }

    public static String streamNameFor(String matchId) {
        return "sets-" + matchId;
    }

    private void when(Event e) {
        if (GamePlayerOne.class.isInstance(e)) {
            gamesPlayerOne++;
            updateScore();
        }
        if (GamePlayerTwo.class.isInstance(e)) {
            gamesPlayerTwo++;
            updateScore();
        }
    }

    private void updateScore() {
        score = String.format("%s-%s", gamesPlayerOne, gamesPlayerTwo);
        checkForWin();
    }

    private void checkForWin() {
        final boolean playerOneHasWonEnoughGames = gamesPlayerOne >= 6;
        final boolean playerTwoIsAtLeastTwoPointsBehind = gamesPlayerOne - gamesPlayerTwo >= 2;
        if (playerOneHasWonEnoughGames && playerTwoIsAtLeastTwoPointsBehind) {
            eventStore.write(Match.streamNameFor(matchId), new SetPlayerOne(score()));
        }

        final boolean playerTwoHasEnoughPoints = gamesPlayerTwo >= 6;
        final boolean playerOneIsAtLeastTwoPointsBehind = gamesPlayerTwo - gamesPlayerOne >= 2;
        if (playerTwoHasEnoughPoints && playerOneIsAtLeastTwoPointsBehind) {
            eventStore.write(Match.streamNameFor(matchId), new SetPlayerTwo(score()));
        }
    }

    public String score() {
        return this.score;
    }
}
