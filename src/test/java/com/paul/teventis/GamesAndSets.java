package com.paul.teventis;

import com.paul.teventis.game.Game;
import com.paul.teventis.game.PlayerOneScored;
import com.paul.teventis.set.Set;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class GamesAndSets {

    @Test
    public void canInteractViaTheStore() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();

        final Set set = new Set(inMemoryEventStream, matchId);
        new Game(inMemoryEventStream, matchId);

        inMemoryEventStream.write(Game.streamNameFor(matchId), new PlayerOneScored());
        inMemoryEventStream.write(Game.streamNameFor(matchId), new PlayerOneScored());
        inMemoryEventStream.write(Game.streamNameFor(matchId), new PlayerOneScored());
        inMemoryEventStream.write(Game.streamNameFor(matchId), new PlayerOneScored());
        inMemoryEventStream.write(Game.streamNameFor(matchId), new PlayerOneScored());
        inMemoryEventStream.write(Game.streamNameFor(matchId), new PlayerOneScored());
        inMemoryEventStream.write(Game.streamNameFor(matchId), new PlayerOneScored());
        inMemoryEventStream.write(Game.streamNameFor(matchId), new PlayerOneScored());

        assertThat(set.score()).isEqualTo("2-0");
    }
}
