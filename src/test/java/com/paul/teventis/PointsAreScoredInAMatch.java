package com.paul.teventis;

import com.paul.teventis.events.EventStore;
import com.paul.teventis.game.PlayerOneScored;
import com.paul.teventis.game.PlayerTwoScored;
import com.paul.teventis.match.Match;
import com.paul.teventis.match.SetScore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PointsAreScoredInAMatch {

    private String reportedScore = "";
    private List<SetScore> setScores = new ArrayList<>();

    @Test
    public void andItReportsTheCurrentGameScore() {
        String matchId = UUID.randomUUID().toString();
        final FakeEventStore inMemoryEventStream = new FakeEventStore();

        final Match match = new Match(inMemoryEventStream, matchId);
        match.subscribeToGameScore(s -> reportedScore = s);

        inMemoryEventStream.write(Match.streamNameFor(matchId), new PlayerOneScored());
        assertThat(reportedScore).isEqualTo("15-love");

        inMemoryEventStream.write(Match.streamNameFor(matchId), new PlayerTwoScored());
        assertThat(reportedScore).isEqualTo("15-all");
    }

    @Test
    public void andItReportsThisSetsGamesScores() {
        String matchId = UUID.randomUUID().toString();
        final FakeEventStore inMemoryEventStore = new FakeEventStore();

        final Match match = new Match(inMemoryEventStore, matchId);
        match.subscribeToWinningScores(s -> this.setScores = s);

        playerOneWinsAGame(inMemoryEventStore, Match.streamNameFor(matchId));
        playerTwoWinsAGame(inMemoryEventStore, Match.streamNameFor(matchId));

        assertThat(this.setScores).containsExactly(new SetScore(1, 1));
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
