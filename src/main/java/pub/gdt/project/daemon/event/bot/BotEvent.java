package pub.gdt.project.daemon.event.bot;

import pub.gdt.project.daemon.bot.Bot;
import pub.gdt.project.daemon.event.Event;

public abstract class BotEvent extends Event<Bot> {
    private final long internalEventId;

    public BotEvent(Bot source, long internalEventId) {
        super(source);
        this.internalEventId = internalEventId;
    }

    public long getInternalEventId() {
        return internalEventId;
    }
}
