package pub.gdt.project.daemon.event.server;

import pub.gdt.project.daemon.basic.Server;
import pub.gdt.project.daemon.event.Event;

public abstract class ServerEvent extends Event<Server> {
    public ServerEvent(Server source) {
        super(source);
    }
}
