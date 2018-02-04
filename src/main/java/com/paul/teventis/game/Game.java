package com.paul.teventis.game;

import com.paul.teventis.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Game {

    private GameScore tennisScore = GameScore.LoveAll;
    private List<Consumer<String>> subscriptions = new ArrayList<>();

    public void when(Event e) {
        if (tennisScore.someoneHasWon()) {
            tennisScore = GameScore.LoveAll;
        }

        if (e instanceof PlayerOneScored) {
            tennisScore = tennisScore.pointPlayerOne();
        }

        if (e instanceof PlayerTwoScored) {
            tennisScore = tennisScore.pointPlayerTwo();
        }

        updateSubscribers();
    }

    private void updateSubscribers() {
        subscriptions.forEach(sub -> sub.accept(this.tennisScore.toString()));
    }

    public void subscribeToScore(Consumer<String> subscription) {
        this.subscriptions.add(subscription);
        updateSubscribers();
    }
}
