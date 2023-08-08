package pub.gdt.project.daemon.event.ws.auth;

import org.java_websocket.WebSocket;

public final class ServerConnectAttemptEvent extends AuthEvent {
    private final String serverToken;

    public ServerConnectAttemptEvent(WebSocket source, String serverToken) {
        super(source);
        this.serverToken = serverToken;
    }

    public String getServerToken() {
        return serverToken;
    }
}
