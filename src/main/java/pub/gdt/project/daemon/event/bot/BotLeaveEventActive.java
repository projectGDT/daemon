package pub.gdt.project.daemon.event.bot;

import pub.gdt.project.daemon.bot.Bot;

public final class BotLeaveEventActive extends GroupEvent {
    public BotLeaveEventActive(Bot source, long internalEventId, long groupId) {
        super(source, internalEventId, groupId);
    }
}
