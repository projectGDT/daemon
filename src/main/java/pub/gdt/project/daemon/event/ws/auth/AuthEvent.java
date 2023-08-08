package pub.gdt.project.daemon.event.ws.auth;

import org.java_websocket.WebSocket;
import pub.gdt.project.daemon.event.ws.WebSocketEvent;

public abstract class AuthEvent extends WebSocketEvent {
    public AuthEvent(WebSocket source) {
        super(source);
    }
}
