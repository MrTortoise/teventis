package com.paul.teventis;

import com.paul.teventis.game.Game;
import com.paul.teventis.game.PlayerOneScored;
import com.paul.teventis.game.PlayerTwoScored;
import com.paul.teventis.match.Set;
import com.paul.teventis.match.SetScore;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
                        0-0
                       /   \
                    1-0     0-1
                   /   \   /   \
                2-0     1-1     0-2
               /  \    /   \   /   \
            3-0     2-1     1-2     0-3
           /   \   /  \    /   \   /   \
        4-0     3-1     2-2     1-3     0-4
       /   \   /   \   /   \   /   \   /   \
     5-0    4-1     3-2     2-3     1-4    0-5
    /   \  /   \   /   \   /   \   /   \  /   \
 *6-0*   5-1    4-2     3-3     2-4    1-5    *0-6*
        /   \  /   \   /   \   /   \  /   \
    *6-1*    5-2    4-3     3-4    2-5     *1-6*
            /   \  /   \   /   \  /   \
         *6-2*  5-3     4-4     3-5   *2-6*
               /   \   /   \   /   \
          *6-3*     5-4     4-5     *3-6*
                   /   \   /   \
              *6-4*     5-5     *4-6*
                       /   \
                    6-5     5-6
                   /   \   /   \
              *7-5*     6-6     *5-7*
                       /   \
                    7-6     6-7
                   /   \   /  \
               *8-6*    7-7    *6-8*
                       /   \
                  repeating forever

      sets are a state machine but the possible states carry on forever

      all you need to do is:
        track sets won,
        whether anyone has won 6 or more
        whether the person with the max score is 2 ahead
 */
public class SetsAreWon {
    private List<SetScore> reportedSetScore;

    @Test
    public void byTheFirstPlayerToWinSixPoints() {
        final Game game = new Game();
        final Set set = new Set(game);
        set.subscribeToSetScores(s -> this.reportedSetScore = s);

        playerOneWinsSet(game);
        playerOneWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly(new SetScore(6, 0), new SetScore(1, 0));
    }

    @Test
    public void byEitherPlayer() {
        final Game game = new Game();
        final Set set = new Set(game);
        set.subscribeToSetScores(s -> this.reportedSetScore = s);

        playerOneWinsSet(game);
        playerTwoWinsSet(game);
        playerTwoWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly(new SetScore(6, 0), new SetScore(0, 6), new SetScore(0, 1));
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

        assertThat(this.reportedSetScore).containsExactly(new SetScore(3, 2));

        playerOneWinsGame(game);
        playerTwoWinsGame(game);
        playerTwoWinsGame(game);
        playerTwoWinsGame(game);
        playerOneWinsGame(game);
        playerOneWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly(new SetScore(6, 5));

        playerOneWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly(new SetScore(7, 5));

        playerOneWinsGame(game);

        assertThat(this.reportedSetScore).containsExactly(new SetScore(7, 5), new SetScore(1, 0));
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
