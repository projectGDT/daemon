package pub.gdt.project.daemon.bot;

import pub.gdt.project.daemon.basic.Server;
import pub.gdt.project.daemon.event.bot.BotInvitedJoinGroupRequestEvent;
import pub.gdt.project.daemon.event.bot.MemberJoinRequestEvent;
import pub.gdt.project.daemon.event.bot.NewFriendRequestEvent;

public interface Bot {
    long getQQ();
    String getSessionKey();

    void sendFriendMessage(long friendId, MessageChain messageChain);
    void sendGroupMessage(long groupId, MessageChain messageChain);
    void acceptNewFriendRequest(NewFriendRequestEvent event);
    void rejectNewFriendRequest(NewFriendRequestEvent event, String message);
    void acceptMemberJoinRequest(MemberJoinRequestEvent event);
    void rejectMemberJoinRequest(MemberJoinRequestEvent event, String message);
    void acceptGroupInvitation(BotInvitedJoinGroupRequestEvent event);
    void rejectGroupInvitation(BotInvitedJoinGroupRequestEvent event);

    default void sendMessageToServerGroup(Server server, MessageChain messageChain) {
        sendGroupMessage(server.getGroupId(), messageChain);
    }

    default void broadcastMessage() {
        // get all servers and send message one by one
    }
}
