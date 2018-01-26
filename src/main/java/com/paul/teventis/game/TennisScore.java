package com.paul.teventis.game;

interface TennisScore {

    TennisScore when(PlayerOneScored e);
    TennisScore when(PlayerTwoScored e);
}

class GameScore implements TennisScore {
    private static final TennisScore GamePlayerOne = new GamePlayerOne();
    private static final TennisScore GamePlayerTwo = new GamePlayerTwo();

    private static class AdvantagePlayerOne implements TennisScore {
        public String toString() {
            return "advantage player one";
        }

        public TennisScore when(final PlayerOneScored e) {
            return GameScore.GamePlayerOne;
        }

        public TennisScore when(final PlayerTwoScored e) {
            return GameScore.Deuce;
        }
    }

    private static class AdvantagePlayerTwo implements TennisScore {
        @Override
        public String toString() {
            return "advantage player two";
        }

        public TennisScore when(final PlayerTwoScored e) {
            return GameScore.GamePlayerTwo;
        }

        public TennisScore when(final PlayerOneScored e) {
            return GameScore.Deuce;
        }
    }

    // ugh can't instantiate the items that cycle from deuce through advantage to game because of the cycle
    // :(

    private static final TennisScore AdvantagePlayerOne = new AdvantagePlayerOne();
    private static final TennisScore AdvantagePlayerTwo = new AdvantagePlayerTwo();

    private static final TennisScore Deuce = new GameScore("deuce", AdvantagePlayerOne, AdvantagePlayerTwo);
    private static final TennisScore ThirtyForty = new GameScore("30-40", Deuce, GamePlayerTwo);
    private static final TennisScore FortyThirty = new GameScore("40-30", GamePlayerOne, Deuce);
    private static final TennisScore FifteenForty = new GameScore("15-40", ThirtyForty, GamePlayerTwo);
    private static final TennisScore FortyFifteen = new GameScore("40-15", GamePlayerOne, FortyThirty);
    private static final TennisScore LoveForty = new GameScore("love-40", FifteenForty, GamePlayerTwo);
    private static final TennisScore FortyLove = new GameScore("40-love", GamePlayerOne, FortyFifteen);
    private static final TennisScore ThirtyAll = new GameScore("30-all", FortyThirty, ThirtyForty);
    private static final TennisScore ThirtyFifteen = new GameScore("30-15", FortyFifteen, ThirtyAll);
    private static final TennisScore FifteenThirty = new GameScore("15-30", ThirtyAll, FifteenForty);
    private static final TennisScore LoveThirty = new GameScore("love-30", FifteenThirty, LoveForty);
    private static final TennisScore ThirtyLove = new GameScore("30-love", FortyLove, ThirtyFifteen);
    private static final TennisScore FifteenAll = new GameScore("15-all", ThirtyFifteen, FifteenThirty);
    private static final TennisScore LoveFifteen = new GameScore("love-15", FifteenAll, LoveThirty);
    private static final TennisScore FifteenLove = new GameScore("15-love", ThirtyLove, FifteenAll);
    static final TennisScore LoveAll = new GameScore("love all", FifteenLove, LoveFifteen);

    private final TennisScore onPlayerOneScored;
    private final TennisScore onPlayerTwoScored;
    private final String description;

    private GameScore(String description, TennisScore onPlayerOneScored, TennisScore onPlayerTwoScored) {
        this.description = description;
        this.onPlayerOneScored = onPlayerOneScored;
        this.onPlayerTwoScored = onPlayerTwoScored;
    }

    @Override
    public TennisScore when(PlayerOneScored e) {
        return onPlayerOneScored;
    }

    @Override
    public TennisScore when(PlayerTwoScored e) {
        return onPlayerTwoScored;
    }

    @Override
    public String toString() {
        return description;
    }
}

