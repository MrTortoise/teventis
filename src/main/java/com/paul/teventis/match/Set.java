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
    private List<SetScore> sets = new ArrayList<>();

    private List<Consumer<List<SetScore>>> subscriptions = new ArrayList<>();

    public Set(Game game) {
        game.subscribeToScore(this::onGameScore);
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
        final SetScore setScore = new SetScore(playerOneGames, playerTwoGames);
        if (currentSet >= sets.size()) {
            sets.add(currentSet, setScore);
        } else {
            sets.set(currentSet, setScore);
        }
        updateSubscribers();
    }

    private void updateSubscribers() {
        final ImmutableList<SetScore> scores = ImmutableList.copyOf(sets);
        this.subscriptions.forEach(sub -> sub.accept(scores));
    }

    public void subscribeToSetScores(Consumer<List<SetScore>> subscription) {
        this.subscriptions.add(subscription);
        updateSubscribers();
    }

}
