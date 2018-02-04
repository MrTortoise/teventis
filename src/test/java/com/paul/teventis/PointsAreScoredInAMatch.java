package com.paul.teventis;

import com.paul.teventis.game.PlayerOneScored;
import com.paul.teventis.game.PlayerTwoScored;
import com.paul.teventis.match.Match;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PointsAreScoredInAMatch {

    private String reportedScore = "";

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
}
