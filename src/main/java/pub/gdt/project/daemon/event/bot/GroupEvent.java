package pub.gdt.project.daemon.event.bot;

import pub.gdt.project.daemon.bot.Bot;

public abstract class GroupEvent extends BotEvent {
    private final long groupId;

    public GroupEvent(Bot source, long internalEventId, long groupId) {
        super(source, internalEventId);
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }
}
