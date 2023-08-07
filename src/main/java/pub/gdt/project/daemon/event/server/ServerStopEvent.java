package pub.gdt.project.daemon.event.server;

import pub.gdt.project.daemon.basic.Server;

public class ServerStopEvent extends ServerEvent {
    public ServerStopEvent(Server source) {
        super(source);
    }
}
