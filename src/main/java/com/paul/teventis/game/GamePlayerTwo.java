package com.paul.teventis.game;

public class GamePlayerTwo implements GameWon {
    @Override
    public String toString() {
        return "Game player two";
    }

    public GameScore pointPlayerOne() {
        //ugh
        throw new CannotScoreAfterGameIsWon();
    }

    public GameScore pointPlayerTwo() {
        //ugh
        throw new CannotScoreAfterGameIsWon();
    }
}
