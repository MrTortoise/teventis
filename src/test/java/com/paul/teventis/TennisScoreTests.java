package com.paul.teventis;

import com.paul.teventis.game.CannotScoreAfterGameIsWon;
import com.paul.teventis.game.GamePlayerOne;
import com.paul.teventis.game.GamePlayerTwo;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class TennisScoreTests {
    @Test
    public void playerOneCannotScoreAfterTheyWinTheGame() {
        final Throwable ex = catchThrowable(() -> new GamePlayerOne().pointPlayerOne());
        assertThat(ex).isInstanceOf(CannotScoreAfterGameIsWon.class);
    }

    @Test
    public void playerOneCannotScoreAfterPlayerTwoWinsTheGame() {
        final Throwable ex = catchThrowable(() -> new GamePlayerTwo().pointPlayerOne());
        assertThat(ex).isInstanceOf(CannotScoreAfterGameIsWon.class);
    }

    @Test
    public void playerTwoCannotScoreAfterTheyWinTheGame() {
        final Throwable ex = catchThrowable(() -> new GamePlayerTwo().pointPlayerTwo());
        assertThat(ex).isInstanceOf(CannotScoreAfterGameIsWon.class);
    }

    @Test
    public void playerTwoCannotScoreAfterPlayerOneWinsTheGame() {
        final Throwable ex = catchThrowable(() -> new GamePlayerOne().pointPlayerTwo());
        assertThat(ex).isInstanceOf(CannotScoreAfterGameIsWon.class);
    }
}
