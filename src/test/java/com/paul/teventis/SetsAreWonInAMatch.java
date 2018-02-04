package com.paul.teventis;

import com.paul.teventis.events.EventStore;
import com.paul.teventis.game.PlayerOneScored;
import com.paul.teventis.game.PlayerTwoScored;
import com.paul.teventis.match.Match;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SetsAreWonInAMatch {
    private List<String> setScores;

    @Test
    public void andItReportsAllSetsGamesScores() {
        String matchId = UUID.randomUUID().toString();
        final FakeEventStore inMemoryEventStore = new FakeEventStore();

        final Match match = new Match(inMemoryEventStore, matchId);
        match.subscribeToWinningScores(s -> this.setScores = s);

        playerOneWinsAGame(inMemoryEventStore, Match.streamNameFor(matchId));
        playerTwoWinsAGame(inMemoryEventStore, Match.streamNameFor(matchId));

        assertThat(this.setScores).containsExactly("1-1");
    }

    private void playerTwoWinsAGame(EventStore inMemoryEventStore, String streamName) {
        inMemoryEventStore.write(streamName, new PlayerTwoScored());
        inMemoryEventStore.write(streamName, new PlayerTwoScored());
        inMemoryEventStore.write(streamName, new PlayerTwoScored());
        inMemoryEventStore.write(streamName, new PlayerTwoScored());
    }

    private void playerOneWinsAGame(EventStore inMemoryEventStore, String streamName) {
        inMemoryEventStore.write(streamName, new PlayerOneScored());
        inMemoryEventStore.write(streamName, new PlayerOneScored());
        inMemoryEventStore.write(streamName, new PlayerOneScored());
        inMemoryEventStore.write(streamName, new PlayerOneScored());
    }
}
