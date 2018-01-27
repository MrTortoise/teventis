package com.paul.teventis.games;

import com.google.common.collect.ImmutableList;
import com.paul.teventis.FakeEventStore;
import com.paul.teventis.events.Event;
import com.paul.teventis.game.Game;
import com.paul.teventis.game.GameWon;
import com.paul.teventis.game.PlayerOneScored;
import com.paul.teventis.set.Set;
import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class UnexpectedEvents {
    private class HoleInOne implements Event {

    }
    @Test
    public void doNotMakeTheGameWinMultipleTimes() {
        final ImmutableList<Event> events = ImmutableList.of(
            new PlayerOneScored(),
            new PlayerOneScored(),
            new PlayerOneScored(),
            new PlayerOneScored(),
            new HoleInOne());

        final FakeEventStore inMemoryEventStream = new FakeEventStore();
        final Game game = new Game(inMemoryEventStream, "arbitraryGuid");
        events.forEach(game::when);

        final Stream<Event> gameWonEvents = inMemoryEventStream.readAll(Set.streamNameFor("arbitraryGuid"))
                                                .stream()
                                                .filter(GameWon.class::isInstance);
        assertThat(gameWonEvents).hasSize(1);

    }
}
