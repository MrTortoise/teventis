package com.paul.teventis.match;

public class SetScore {
    private final int playerOneGames;
    private final int playerTwoGames;

    public SetScore(int playerOneGames, int playerTwoGames) {
        this.playerOneGames = playerOneGames;
        this.playerTwoGames = playerTwoGames;
    }

    @Override
    public String toString() {
        return String.format("%s-%s", playerOneGames, playerTwoGames);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetScore other = (SetScore) o;

        return playerOneGames == other.playerOneGames && playerTwoGames == other.playerTwoGames;
    }

    @Override
    public int hashCode() {
        int result = playerOneGames;
        result = 31 * result + playerTwoGames;
        return result;
    }

    public int getPlayerOneGames() {
        return playerOneGames;
    }

    public int getPlayerTwoGames() {
        return playerTwoGames;
    }
}
