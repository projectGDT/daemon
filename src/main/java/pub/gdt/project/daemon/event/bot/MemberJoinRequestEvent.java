package pub.gdt.project.daemon.event.bot;

import pub.gdt.project.daemon.bot.Bot;

public final class MemberJoinRequestEvent extends GroupEvent {
    public static final int RESPONSE_ACCEPT = 0;
    public static final int RESPONSE_REJECT = 1;

    private final long fromId;
    
    public MemberJoinRequestEvent(Bot source, long internalEventId, long groupId, long fromId) {
        super(source, internalEventId, groupId);
        this.fromId = fromId;
    }

    public long getFromId() {
        return fromId;
    }

    public void accept() {
        getSource().acceptMemberJoinRequest(this);
    }

    public void reject(String message) {
        getSource().rejectMemberJoinRequest(this, message);
    }
}
