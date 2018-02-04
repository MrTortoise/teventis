package com.paul.teventis;

import com.paul.teventis.events.Event;
import com.paul.teventis.events.EventStore;
import com.paul.teventis.game.PlayerOneScored;
import com.paul.teventis.game.PlayerTwoScored;
import com.paul.teventis.match.Match;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchesAreWon {
    private String matchWon;

    @Test
    public void ifOnePlayerWinsThreeSetsAndTheOtherNeverScores() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final EventStore eventStore = new FakeEventStore();
        final String matchId = UUID.randomUUID().toString();
        final Match match = new Match(eventStore, matchId);

        match.subscribeToMatchWon(s -> matchWon = s);

        playerWinsMatch(eventStore, matchId, PlayerOneScored.class);

        assertThat(this.matchWon).isEqualTo("Game, Set, and Match to player one");
    }

//    @Test
//    public void bestOfFiveOtherwise() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        final EventStore eventStore = new FakeEventStore();
//        final String matchId = UUID.randomUUID().toString();
//        final Match match = new Match(eventStore, matchId);
//
//        match.subscribeToMatchWon(s -> matchWon = s);
//
////        playerWinsSet(eventStore, matchId, PlayerOneScored.class);
////        playerWinsSet(eventStore, matchId, PlayerTwoScored.class);
////        playerWinsSet(eventStore, matchId, PlayerOneScored.class);
////        playerWinsSet(eventStore, matchId, PlayerTwoScored.class);
////        playerWinsSet(eventStore, matchId, PlayerOneScored.class);
////        playerWinsSet(eventStore, matchId, PlayerOneScored.class);
//
//        assertThat(this.matchWon).isEqualTo("Game, Set, and Match to player one");
//    }

    private <T extends Event> void playerWinsMatch(EventStore eventStore, String matchId, Class<T> playerScored) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        for(int i = 0; i < 3; i++) {
            playerWinsSet(eventStore, Match.streamNameFor(matchId), playerScored);
        }
    }

    private <T extends Event> void playerWinsSet(EventStore eventStore, String streamName, Class<T> playerScored) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for(int i = 0; i < 6; i++) {
            playerWinsGame(eventStore, streamName, playerScored);
        }
    }

    private <T extends Event> void playerWinsGame(EventStore eventStore, String streamName, Class<T> eventClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        for (int i = 0; i < 4; i++) {
            eventStore.write(streamName, eventClass.getDeclaredConstructor().newInstance());
        }
    }
}
