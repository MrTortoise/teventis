package com.paul.teventis.match;

import com.google.common.collect.ImmutableList;
import com.paul.teventis.FakeEventStore;
import com.paul.teventis.events.Event;
import com.paul.teventis.set.Set;
import com.paul.teventis.set.SetPlayerOne;
import com.paul.teventis.set.SetPlayerTwo;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchesCanBeWon {
    @Test
    public void playerOneCanWinAMatchIfPlayerTwoNeverScoresAPoint() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();
        inMemoryEventStream.writeAll(Set.streamNameFor(matchId), ImmutableList.of(
            new SetPlayerOne("6-0"),
            new SetPlayerOne("6-0"),
            new SetPlayerOne("6-0")
        ));

        new Match(inMemoryEventStream, matchId);

        final Event event = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(event).isInstanceOf(MatchPlayerOne.class);
    }

    @Test
    public void playerTwoCanWinAMatchIfPlayerTwoNeverScoresAPoint() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();
        inMemoryEventStream.writeAll(Set.streamNameFor(matchId), ImmutableList.of(
            new SetPlayerTwo("6-0"),
            new SetPlayerTwo("6-0"),
            new SetPlayerTwo("6-0")
        ));

        new Match(inMemoryEventStream, matchId);

        final Event event = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(event).isInstanceOf(MatchPlayerTwo.class);
    }

    @Test
    public void playerOneCanWinAMatchBestOfFive() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();
        inMemoryEventStream.writeAll(Set.streamNameFor(matchId), ImmutableList.of(
            new SetPlayerOne("6-0"),
            new SetPlayerOne("6-0"),
            new SetPlayerTwo("6-0")
        ));

        new Match(inMemoryEventStream, matchId);

        final Event event = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(event).isNotInstanceOf(MatchPlayerOne.class);

        inMemoryEventStream.write(Set.streamNameFor(matchId), new SetPlayerOne("6-0"));

        final Event matchWin = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(matchWin).isInstanceOf(MatchPlayerOne.class);
    }

    @Test
    public void playerTwoCanWinAMatchBestOfFive() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();
        inMemoryEventStream.writeAll(Set.streamNameFor(matchId), ImmutableList.of(
            new SetPlayerOne("6-0"),
            new SetPlayerTwo("6-0"),
            new SetPlayerOne("6-0")
        ));

        new Match(inMemoryEventStream, matchId);

        final Event event = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(event).isNotInstanceOf(MatchPlayerOne.class);

        inMemoryEventStream.write(Set.streamNameFor(matchId), new SetPlayerTwo("6-0"));
        inMemoryEventStream.write(Set.streamNameFor(matchId), new SetPlayerTwo("6-0"));
        inMemoryEventStream.write(Set.streamNameFor(matchId), new SetPlayerTwo("6-0"));
        inMemoryEventStream.write(Set.streamNameFor(matchId), new SetPlayerTwo("6-0"));

        final Event matchWin = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(matchWin).isInstanceOf(MatchPlayerTwo.class);
    }

    @Test
    public void playerOneAsTheOnlyScoringPlayerCannotWinAfterOneSet() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();
        inMemoryEventStream.writeAll(Set.streamNameFor(matchId), ImmutableList.of(
            new SetPlayerOne("6-0")
        ));

        new Match(inMemoryEventStream, matchId);

        final Event event = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(event).isNotInstanceOf(MatchPlayerOne.class);
    }

    @Test
    public void playerOneAsTheOnlyScoringPlayerCannotWinAfterTwoSets() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();
        inMemoryEventStream.writeAll(Set.streamNameFor(matchId), ImmutableList.of(
            new SetPlayerOne("6-0"),
            new SetPlayerOne("6-0")
        ));

        new Match(inMemoryEventStream, matchId);

        final Event event = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(event).isNotInstanceOf(MatchPlayerOne.class);
    }

    @Test
    public void playerTwoAsTheOnlyScoringPlayerCannotWinAfterOneSet() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();
        inMemoryEventStream.writeAll(Set.streamNameFor(matchId), ImmutableList.of(
            new SetPlayerTwo("6-0")
        ));

        new Match(inMemoryEventStream, matchId);

        final Event event = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(event).isNotInstanceOf(MatchPlayerOne.class);
    }

    @Test
    public void playerTwoAsTheOnlyScoringPlayerCannotWinAfterTwoSets() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();
        inMemoryEventStream.writeAll(Set.streamNameFor(matchId), ImmutableList.of(
            new SetPlayerTwo("6-0"),
            new SetPlayerTwo("6-0")
        ));

        new Match(inMemoryEventStream, matchId);

        final Event event = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(event).isNotInstanceOf(MatchPlayerTwo.class);
    }

    @Test
    public void alternatingSetsMeansNobodyWins() {
        String matchId = UUID.randomUUID().toString();

        final FakeEventStore inMemoryEventStream = new FakeEventStore();
        inMemoryEventStream.writeAll(Set.streamNameFor(matchId), ImmutableList.of(
            new SetPlayerOne("6-0"),
            new SetPlayerTwo("6-0"),
            new SetPlayerOne("6-0"),
            new SetPlayerTwo("6-0"),
            new SetPlayerOne("6-0"),
            new SetPlayerTwo("6-0"),
            new SetPlayerOne("6-0"),
            new SetPlayerTwo("6-0")
        ));

        new Match(inMemoryEventStream, matchId);

        final Event event = inMemoryEventStream.readLast(Set.streamNameFor(matchId));
        assertThat(event).isNotInstanceOf(MatchPlayerTwo.class);
        assertThat(event).isNotInstanceOf(MatchPlayerOne.class);
    }

}

