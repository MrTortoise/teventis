package com.paul.teventis.match;

import com.google.common.collect.ImmutableList;
import com.paul.teventis.game.Game;
import com.paul.teventis.game.GamePlayerOne;
import com.paul.teventis.game.GamePlayerTwo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Set {

    private int playerTwoGames = 0;
    private int playerOneGames = 0;
    private int currentSet = 0;
    private List<String> sets = new ArrayList<>();

    private List<Consumer<List<String>>> subscriptions = new ArrayList<>();

    public Set(Game game) {
        game.subscribeToScore(this::onGameScore);
    }

    private String setScore() {
        return String.format("%s-%s", playerOneGames, playerTwoGames);
    }

    private void onGameScore(String gameScore) {
        if (gameScore.equals(new GamePlayerOne().toString())) {
            this.playerOneGames++;

        }
        else if (gameScore.equals(new GamePlayerTwo().toString())) {
            this.playerTwoGames++;
        }

        reportScore();
        checkForSetWon();
    }

    private void checkForSetWon() {
        if (playerOneHasWon() || playerTwoHasWon()) {
            playerOneGames = 0;
            playerTwoGames = 0;
            currentSet++;
        }
    }

    private boolean playerTwoHasWon() {
        return playerHasWon(playerTwoGames, playerOneGames);
    }

    private boolean playerOneHasWon() {
        return playerHasWon(playerOneGames, playerTwoGames);
    }

    private boolean playerHasWon(int scoreOne, int scoreTwo) {
        boolean hasScoredAtLeastSix = scoreOne >=6;
        boolean isTwoClearOfTheOtherPlayer = scoreOne - scoreTwo >= 2;
        return hasScoredAtLeastSix && isTwoClearOfTheOtherPlayer;
    }

    private void reportScore() {
        if (currentSet >= sets.size()) {
            sets.add(currentSet, setScore());
        } else {
            sets.set(currentSet, setScore());
        }
        final ImmutableList<String> scores = ImmutableList.copyOf(sets);
        this.subscriptions.forEach(sub -> sub.accept(scores));
    }

    public void subscribeToSetScores(Consumer<List<String>> subscription) {
        this.subscriptions.add(subscription);
    }
}
