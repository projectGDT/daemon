package pub.gdt.project.daemon.event.player;

import pub.gdt.project.daemon.basic.Player;

public final class PlayerAddJavaProfileAttemptEvent extends PlayerEvent {
    private final String authCode;

    public PlayerAddJavaProfileAttemptEvent(Player source, String authCode) {
        super(source);
        this.authCode = authCode;
    }

    public String getAuthCode() {
        return authCode;
    }
}
