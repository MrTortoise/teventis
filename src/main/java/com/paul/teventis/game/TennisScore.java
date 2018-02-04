package com.paul.teventis.game;

import com.google.common.collect.ImmutableMap;


/*

                        0-0
                       /   \
                     15-0  0-15
                    /   \ /    \
                30-0   15-15    0-30
               /   \   /    \   /   \
            40-0    30-15  15-30    0-40
           /   \        \  /       /    \
         GP1    40-15   30-all    15-40  GP2
                /   \   /   \   /     \
              GP1   40-30   30-40      GP2
                    /   \  /    \
                 GP1    deuce    GP2
                       //   \\
                      AP1   AP2
                     /         \
                   GP1          GP2
 */
class GameScore {

    private static final ImmutableMap<String, String> playerOneScoreTransitions = ImmutableMap.<String, String>builder()
                                                                .put("love all", "15-love")
                                                                .put("love-15", "15-all")
                                                                .put("15-love", "30-love")
                                                                .put("30-love", "40-love")
                                                                .put("15-all", "30-15")
                                                                .put("love-30", "15-30")
                                                                .put("40-love", "Game player one")
                                                                .put("30-15", "40-15")
                                                                .put("15-30", "30-all")
                                                                .put("love-40", "15-40")
                                                                .put("40-15", "Game player one")
                                                                .put("30-all", "40-30")
                                                                .put("15-40", "30-40")
                                                                .put("40-30", "Game player one")
                                                                .put("30-40", "deuce")
                                                                .put("deuce", "advantage player one")
                                                                .put("advantage player one", "Game player one")
                                                                .put("advantage player two", "deuce")
                                                                .build();

    private static final ImmutableMap<String, String> playerTwoScoreTransitions = ImmutableMap.<String, String>builder()
                                                                .put("love all", "love-15")
                                                                .put("love-15", "love-30")
                                                                .put("15-love", "15-all")
                                                                .put("30-love", "30-15")
                                                                .put("15-all", "15-30")
                                                                .put("love-30", "love-40")
                                                                .put("40-love", "40-15")
                                                                .put("30-15", "30-all")
                                                                .put("15-30", "15-40")
                                                                .put("love-40", "Game player two")
                                                                .put("40-15", "40-30")
                                                                .put("30-all", "30-40")
                                                                .put("15-40", "Game player two")
                                                                .put("40-30", "deuce")
                                                                .put("30-40", "Game player two")
                                                                .put("deuce", "advantage player two")
                                                                .put("advantage player one", "deuce")
                                                                .put("advantage player two", "Game player two")
                                                                .build();

    public static GameScore LoveAll = new GameScore("love all");

    private final String description;

    private GameScore(String description) {
        this.description = description;
    }

    public GameScore pointPlayerOne() throws CannotTransitionFromScore {
        return getNextScore(playerOneScoreTransitions);
    }

    public GameScore pointPlayerTwo() throws CannotTransitionFromScore {
        return getNextScore(playerTwoScoreTransitions);
    }

    private GameScore getNextScore(ImmutableMap<String, String> playerStates) throws CannotTransitionFromScore {
        final String next = playerStates.get(this.description);
        if (next == null) {
            throw new CannotTransitionFromScore(this.description);
        }
        return new GameScore(next);
    }

    @Override
    public String toString() {
        return description;
    }

    public boolean someoneHasWon() {
        return this.description.equals("Game player one")
            || this.description.equals("Game player two");
    }

    private static class CannotTransitionFromScore extends Error {
        CannotTransitionFromScore(String score) {
            super("On point scored could not transition from score:" + score);
        }
    }
}

