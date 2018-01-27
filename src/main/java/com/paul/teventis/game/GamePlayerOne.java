package com.paul.teventis.game;

import com.paul.teventis.events.Event;

public class GamePlayerOne implements TennisScore, GameWon {

    public GamePlayerOne() {
    }

    @Override
    public String toString() {
        return "Game player one";
    }

    @Override
    public TennisScore pointPlayerOne() {
        //ugh
        throw new CannotScoreAfterGameIsWon();
    }

    @Override
    public TennisScore pointPlayerTwo() {
        //ugh
        throw new CannotScoreAfterGameIsWon();
    }
}
