package pub.gdt.project.daemon.event.auth;

import org.java_websocket.WebSocket;
import pub.gdt.project.daemon.event.Event;

public abstract class AuthEvent extends Event<WebSocket> {
    public AuthEvent(WebSocket source) {
        super(source);
    }
}
