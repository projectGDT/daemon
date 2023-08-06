package pub.gdt.project.daemon.event.bot;

import pub.gdt.project.daemon.bot.Bot;

public final class NewFriendRequestEvent extends BotEvent {
    private static final long NOT_FROM_GROUP = 0L;

    private final long fromId, groupId;

    public NewFriendRequestEvent(Bot source, long internalEventId, long fromId, long groupId) {
        super(source, internalEventId);
        this.fromId = fromId;
        this.groupId = groupId;
    }

    public NewFriendRequestEvent(Bot source, long internalEventId, long fromId) {
        this(source, internalEventId, fromId, NOT_FROM_GROUP);
    }

    public long getFromId() {
        return fromId;
    }

    public long getGroupId() {
        return groupId;
    }

    public boolean isFromGroup() {
        return groupId == NOT_FROM_GROUP;
    }

    public void accept() {
        getSource().acceptNewFriendRequest(this);
    }

    public void reject(String message) {
        getSource().rejectNewFriendRequest(this, message);
    }
}
