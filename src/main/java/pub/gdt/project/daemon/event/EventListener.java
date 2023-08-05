package pub.gdt.project.daemon.event;

import java.util.function.Consumer;
import java.util.function.Function;

public interface EventListener<T extends Event<?>> extends Function<T, EventListeningStatus>, java.util.EventListener {
    static <T extends Event<?>> EventListener<T> always(Consumer<T> action) {
        return event -> {
            action.accept(event);
            return EventListeningStatus.GO_ON;
        };
    }

    static <T extends Event<?>> EventListener<T> once(Consumer<T> action) {
        return event -> {
            action.accept(event);
            return EventListeningStatus.STOP;
        };
    }
}
