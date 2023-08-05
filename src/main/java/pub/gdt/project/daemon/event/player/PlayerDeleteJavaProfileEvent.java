package pub.gdt.project.daemon.event.player;

import pub.gdt.project.daemon.basic.JavaProfile;
import pub.gdt.project.daemon.basic.Player;

public final class PlayerDeleteJavaProfileEvent extends PlayerEvent {
    private final JavaProfile deletedProfile;

    public PlayerDeleteJavaProfileEvent(Player source, JavaProfile deletedProfile) {
        super(source);
        this.deletedProfile = deletedProfile;
    }

    public JavaProfile getDeletedProfile() {
        return deletedProfile;
    }
}
