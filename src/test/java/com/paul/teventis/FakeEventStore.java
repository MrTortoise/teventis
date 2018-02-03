package com.paul.teventis;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.paul.teventis.events.Event;
import com.paul.teventis.events.EventStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FakeEventStore implements EventStore {
    private final Map<String, List<Event>> eventStreams = new HashMap<>();

    private final Map<String, List<Consumer<Event>>> subscriptions = new HashMap<>();

    @Override
    public ImmutableList<Event> readAll(final String streamName) {
        final List<Event> events = eventStreams.getOrDefault(streamName, new ArrayList<>());
        return ImmutableList.copyOf(events);
    }

    @Override
    public Event readLast(String streamName) {
        return Iterables.getLast(readAll(streamName));
    }

    @Override
    public void write(final String stream, final Event e) {
        final List<Event> events = eventStreams.getOrDefault(stream, new ArrayList<>());
        events.add(e);
        eventStreams.put(stream, events);
        subscriptions.getOrDefault(stream, ImmutableList.of(x -> {})).forEach(x -> x.accept(e));
    }

    @Override
    public void writeAll(final String stream, final ImmutableList<Event> events) {
        events.forEach(e -> this.write(stream, e));
    }

    @Override
    public void subscribe(final String stream, final Consumer<Event> subscription) {
        this.subscriptions.putIfAbsent(stream, new ArrayList<>());
        final List<Consumer<Event>> subscribers = this.subscriptions.get(stream);
        subscribers.add(subscription);
        this.subscriptions.put(stream, subscribers);
    }
}
