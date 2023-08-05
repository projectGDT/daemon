package pub.gdt.project.daemon.event.bot;

public final class MemberJoinRequestEvent extends BotEvent {
    private final long applicantQQ, groupId;
    
    public MemberJoinRequestEvent(Bot source, long internalEventId, long applicantQQ, long groupId) {
        super(source, internalEventId);
        this.applicantQQ = applicantQQ;
        this.groupId = groupId;
    }

    public long getApplicantQQ() {
        return applicantQQ;
    }

    public long getGroupId() {
        return groupId;
    }
}
