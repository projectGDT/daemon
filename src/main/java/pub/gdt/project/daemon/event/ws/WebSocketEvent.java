package pub.gdt.project.daemon.event.ws;

import org.java_websocket.WebSocket;
import pub.gdt.project.daemon.event.Event;

public class WebSocketEvent extends Event<WebSocket> {
    public WebSocketEvent(WebSocket source) {
        super(source);
    }
}
