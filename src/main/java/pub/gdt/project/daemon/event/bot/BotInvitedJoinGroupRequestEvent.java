package pub.gdt.project.daemon.event.bot;

import pub.gdt.project.daemon.bot.Bot;

public final class BotInvitedJoinGroupRequestEvent extends GroupEvent {
    public BotInvitedJoinGroupRequestEvent(Bot source, long internalEventId, long groupId) {
        super(source, internalEventId, groupId);
    }

    public void accept() {
        getSource().acceptGroupInvitation(this);
    }

    public void reject() {
        getSource().rejectGroupInvitation(this);
    }
}
