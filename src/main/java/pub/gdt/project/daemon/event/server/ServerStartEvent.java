package pub.gdt.project.daemon.event.server;

import pub.gdt.project.daemon.basic.Server;

public final class ServerStartEvent extends ServerEvent {
    public ServerStartEvent(Server source) {
        super(source);
    }
}
