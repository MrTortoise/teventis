package com.paul.teventis.game;

public class GamePlayerOne implements GameWon {

    public GamePlayerOne() {
    }

    @Override
    public String toString() {
        return "Game player one";
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
