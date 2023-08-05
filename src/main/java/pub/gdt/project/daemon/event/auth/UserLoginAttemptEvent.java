package pub.gdt.project.daemon.event.auth;

import org.java_websocket.WebSocket;

import java.util.Arrays;

public final class UserLoginAttemptEvent extends AuthEvent {
    private final long qq;
    private final byte[] decryptedPassword;

    public UserLoginAttemptEvent(WebSocket source, long qq, byte[] decryptedPassword) {
        super(source);
        this.qq = qq;
        this.decryptedPassword = decryptedPassword;
    }

    public long getQQ() {
        return qq;
    }

    public byte[] getDecryptedPassword() {
        return decryptedPassword;
    }

    public void clearPasswordArray() {
        Arrays.fill(decryptedPassword, (byte) 0);
    }
}
