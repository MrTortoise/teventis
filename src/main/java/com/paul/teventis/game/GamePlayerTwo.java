package com.paul.teventis.game;

public class GamePlayerTwo implements TennisScore, GameWon {
    @Override
    public String toString() {
        return "Game player two";
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
