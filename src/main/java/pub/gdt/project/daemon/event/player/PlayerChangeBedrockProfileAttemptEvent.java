package pub.gdt.project.daemon.event.player;

import pub.gdt.project.daemon.basic.Player;

public final class PlayerChangeBedrockProfileAttemptEvent extends PlayerEvent {
    private final String verificationCode;

    public PlayerChangeBedrockProfileAttemptEvent(Player source, String verificationCode) {
        super(source);
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}
