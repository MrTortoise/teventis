package com.paul.teventis;

import com.paul.teventis.events.Event;
import com.paul.teventis.game.Game;
import com.paul.teventis.game.PlayerOneScored;
import com.paul.teventis.match.Match;
import com.paul.teventis.match.MatchPlayerOne;
import com.paul.teventis.set.Set;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchesGamesAndSets {

    @Test
    public void canInteractViaTheStore() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();

        final Set set = new Set(inMemoryEventStream, matchId);
        new Game(inMemoryEventStream, matchId);

        final String gameStream = Game.streamNameFor(matchId);
        playerOneWinGame(inMemoryEventStream, gameStream);
        playerOneWinGame(inMemoryEventStream, gameStream);

        assertThat(set.score()).isEqualTo("2-0");
    }
    
    @Test
    public void canWinAMatch() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();

        final Match match = new Match(inMemoryEventStream, matchId);
        final Set set = new Set(inMemoryEventStream, matchId);
        new Game(inMemoryEventStream, matchId);

        final String gameStream = Game.streamNameFor(matchId);
        playerOneWinSet(inMemoryEventStream, gameStream);

        final Event event = inMemoryEventStream.readLast(Match.streamNameFor(matchId));
        assertThat(event).isInstanceOf(MatchPlayerOne.class);
    }

    private void playerOneWinSet(FakeEventStore inMemoryEventStream, String gameStream) {
        playerOneWinGame(inMemoryEventStream, gameStream);
        playerOneWinGame(inMemoryEventStream, gameStream);
        playerOneWinGame(inMemoryEventStream, gameStream);
        playerOneWinGame(inMemoryEventStream, gameStream);
        playerOneWinGame(inMemoryEventStream, gameStream);
        playerOneWinGame(inMemoryEventStream, gameStream);
    }

    private void playerOneWinGame(FakeEventStore inMemoryEventStream, String gameStream) {
        inMemoryEventStream.write(gameStream, new PlayerOneScored());
        inMemoryEventStream.write(gameStream, new PlayerOneScored());
        inMemoryEventStream.write(gameStream, new PlayerOneScored());
        inMemoryEventStream.write(gameStream, new PlayerOneScored());
    }
}
