package main.java.com.cafepos.app.events;

import java.util.*;
import java.util.function.Consumer;

public final class EventsBus
{
    private final Map<Class<?>, List<Consumer<?>>> handlers = new HashMap<>();

    public <T> void on(Class<T> eventType, Consumer<T> consumer)
    {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(consumer);
    }

    @SuppressWarnings("unchecked")
    public <T> void emit(T event)
    {
        var list = handlers.getOrDefault(event.getClass(), List.of());
        for(var consumer : list) ((Consumer<T>) consumer).accept(event);
    }
}
