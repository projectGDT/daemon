package pub.gdt.project.daemon.event.player;

import pub.gdt.project.daemon.basic.BedrockProfile;
import pub.gdt.project.daemon.basic.Player;

public final class PlayerDeleteBedrockProfileEvent extends PlayerEvent {
    private final BedrockProfile deletedProfile;

    public PlayerDeleteBedrockProfileEvent(Player source, BedrockProfile deletedProfile) {
        super(source);
        this.deletedProfile = deletedProfile;
    }

    public BedrockProfile getDeletedProfile() {
        return deletedProfile;
    }
}
