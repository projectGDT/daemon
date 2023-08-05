package pub.gdt.project.daemon.event;

import java.time.Instant;
import java.util.EventObject;

public abstract class Event<T> extends EventObject {
    private final Instant timestamp;

    public Event(T source) {
        super(source);
        timestamp = Instant.now();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getSource() {
        return (T) source;
    }

    public Instant createdAt() {
        return timestamp;
    }
}
