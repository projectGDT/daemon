package pub.gdt.project.daemon.bot;

import pub.gdt.project.daemon.basic.Server;

public interface Bot {
    long getQQ();
    void sendGroupMessage(long groupId, MessageChain messageChain);

    default void sendMessageToServerGroup(Server server, MessageChain messageChain) {
        sendGroupMessage(server.getGroupId(), messageChain);
    }

    default void broadcastMessage() {
        // get all servers and send message one by one
    }
}
