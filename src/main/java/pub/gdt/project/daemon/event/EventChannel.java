package pub.gdt.project.daemon.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public final class EventChannel {
    private static final LinkedBlockingQueue<Event<?>> eventQueue = new LinkedBlockingQueue<>();
    private static final Multimap<Class<? extends Event<?>>, EventListener<? extends Event<?>>> eventListenerMap = HashMultimap.create();

    @SuppressWarnings("all")
    private static final Thread eventDispatchThread = new Thread(() -> {
        try {
            while (true) {
                Event<?> event = eventQueue.take();

                // Log the event

                if (event instanceof StopAcceptingEvent) break; // and the thread ends

                // fire events
                for (EventListener listener : eventListenerMap.get((Class<? extends Event<?>>) event.getClass())) {
                    // Here we are sure that the consumer here accepts the event of this type
                    try {
                        EventListeningStatus status = (EventListeningStatus) listener.apply(event);
                        if (status == EventListeningStatus.STOP) eventListenerMap.remove(event.getClass(), listener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException ignored) {}
    });

    static {
        eventDispatchThread.start();
    }

    public static <T extends Event<?>> void subscribe(Class<T> tClass, EventListener<T> listener) {
        eventListenerMap.put(tClass, listener);
    }

    public static <T extends Event<?>> void subscribeAlways(Class<T> tClass, Consumer<T> action) {
        eventListenerMap.put(tClass, EventListener.always(action));
    }

    public static <T extends Event<?>> void subscribeOnce(Class<T> tClass, Consumer<T> action) {
        eventListenerMap.put(tClass, EventListener.once(action));
    }

    public static <T extends Event<?>> void fire(T event) {
        eventQueue.offer(event);
    }

    public static void stopAccepting() {
        fire(new StopAcceptingEvent());
    }

    private static class StopAcceptingEvent extends SourcelessEvent {}
}
