package pub.gdt.project.daemon.event.ws.auth;

import org.java_websocket.WebSocket;

public final class UserLoginAttemptEvent extends AuthEvent {
    private final long qq;
    private final String decryptedPassword;

    public UserLoginAttemptEvent(WebSocket source, long qq, String decryptedPassword) {
        super(source);
        this.qq = qq;
        this.decryptedPassword = decryptedPassword;
    }

    public long getQQ() {
        return qq;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }
}
