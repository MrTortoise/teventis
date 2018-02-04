package com.paul.teventis;

import com.paul.teventis.game.Game;
import com.paul.teventis.game.PlayerOneScored;
import com.paul.teventis.game.PlayerTwoScored;
import com.paul.teventis.match.Set;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SetsAreWon {
    private List<String> reportedSetScore;

    @Test
    public void byTheFirstPlayerToWinSixPoints() {
        final Game game = new Game();
        final Set set = new Set(game);
        set.subscribeToSetScores(s -> this.reportedSetScore = s);

        playerOneWinsSet(game);
        playerOneWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly("6-0", "1-0");
    }

    @Test
    public void byEitherPlayer() {
        final Game game = new Game();
        final Set set = new Set(game);
        set.subscribeToSetScores(s -> this.reportedSetScore = s);

        playerOneWinsSet(game);
        playerTwoWinsSet(game);
        playerTwoWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly("6-0", "0-6", "0-1");
    }

    @Test
    public void byTheFirstPlayerToBeAtSixPointsAndTwoPointsClear() {
        final Game game = new Game();
        final Set set = new Set(game);
        set.subscribeToSetScores(s -> this.reportedSetScore = s);

        playerOneWinsGame(game);
        playerOneWinsGame(game);
        playerOneWinsGame(game);
        playerTwoWinsGame(game);
        playerTwoWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly("3-2");

        playerOneWinsGame(game);
        playerTwoWinsGame(game);
        playerTwoWinsGame(game);
        playerTwoWinsGame(game);
        playerOneWinsGame(game);
        playerOneWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly("6-5");

        playerOneWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly("7-5");

        playerOneWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly("7-5", "1-0");
    }
    
    private void playerOneWinsSet(Game game) {
        playerOneWinsGame(game);
        playerOneWinsGame(game);
        playerOneWinsGame(game);
        playerOneWinsGame(game);
        playerOneWinsGame(game);
        playerOneWinsGame(game);
    }

    private void playerTwoWinsSet(Game game) {
        playerTwoWinsGame(game);
        playerTwoWinsGame(game);
        playerTwoWinsGame(game);
        playerTwoWinsGame(game);
        playerTwoWinsGame(game);
        playerTwoWinsGame(game);
    }

    private void playerOneWinsGame(Game game) {
        game.when(new PlayerOneScored());
        game.when(new PlayerOneScored());
        game.when(new PlayerOneScored());
        game.when(new PlayerOneScored());
    }

    private void playerTwoWinsGame(Game game) {
        game.when(new PlayerTwoScored());
        game.when(new PlayerTwoScored());
        game.when(new PlayerTwoScored());
        game.when(new PlayerTwoScored());
    }
}
