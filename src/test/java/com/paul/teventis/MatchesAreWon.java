package com.paul.teventis;

import com.paul.teventis.events.Event;
import com.paul.teventis.events.EventStore;
import com.paul.teventis.game.PlayerOneScored;
import com.paul.teventis.game.PlayerTwoScored;
import com.paul.teventis.match.Match;
import com.paul.teventis.match.SetScore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchesAreWon {
    private String matchWon;

    @Test
    public void ifOnePlayerWinsThreeSetsAndTheOtherNeverScores() {
        final EventStore eventStore = new FakeEventStore();
        final String matchId = UUID.randomUUID().toString();
        final Match match = new Match(eventStore, matchId);

        match.subscribeToMatchWon(s -> matchWon = s);

        final String streamName = Match.streamNameFor(matchId);
        playerWinsSet(eventStore, streamName, PlayerOneScored::new);
        playerWinsSet(eventStore, streamName, PlayerOneScored::new);
        playerWinsSet(eventStore, streamName, PlayerOneScored::new);

        assertThat(this.matchWon).isEqualTo("Game, Set, and Match to player one");
    }

    @Test
    public void bestOfFiveOtherwise() {
        final EventStore eventStore = new FakeEventStore();
        final String matchId = UUID.randomUUID().toString();
        final Match match = new Match(eventStore, matchId);

        match.subscribeToMatchWon(s -> matchWon = s);

        final String streamName = Match.streamNameFor(matchId);

        playerWinsSet(eventStore, streamName, PlayerOneScored::new); // 1 0
        playerWinsSet(eventStore, streamName, PlayerTwoScored::new); // 1 1
        playerWinsSet(eventStore, streamName, PlayerOneScored::new); // 2 1

        assertThat(this.matchWon).isNull();

        playerWinsSet(eventStore, streamName, PlayerTwoScored::new); // 2 2
        playerWinsSet(eventStore, streamName, PlayerTwoScored::new); // 2 3

        assertThat(this.matchWon).isEqualTo("Game, Set, and Match to player two");
    }

    private <T extends Event> void playerWinsSet(EventStore eventStore, String streamName, Supplier<T> playerScored) {
        playerWinsGame(eventStore, streamName, playerScored);
        playerWinsGame(eventStore, streamName, playerScored);
        playerWinsGame(eventStore, streamName, playerScored);
        playerWinsGame(eventStore, streamName, playerScored);
        playerWinsGame(eventStore, streamName, playerScored);
        playerWinsGame(eventStore, streamName, playerScored);
    }

    private <T extends Event> void playerWinsGame(EventStore eventStore, String streamName, Supplier<T> playerScored) {
        eventStore.write(streamName, playerScored.get());
        eventStore.write(streamName, playerScored.get());
        eventStore.write(streamName, playerScored.get());
        eventStore.write(streamName, playerScored.get());
    }
}
