package pub.gdt.project.daemon.event.bot;

import pub.gdt.project.daemon.bot.Bot;

public final class MemberJoinEvent extends GroupEvent {
    private final long newMemberId;
    public MemberJoinEvent(Bot source, long internalEventId, long groupId, long newMemberId) {
        super(source, internalEventId, groupId);
        this.newMemberId = newMemberId;
    }

    public long getNewMemberId() {
        return newMemberId;
    }
}
