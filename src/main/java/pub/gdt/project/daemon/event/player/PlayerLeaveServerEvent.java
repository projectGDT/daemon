package pub.gdt.project.daemon.event.player;

import pub.gdt.project.daemon.basic.Player;
import pub.gdt.project.daemon.basic.Server;

public final class PlayerLeaveServerEvent extends PlayerServerEvent {
    public enum Cause {
        PLAYER_DISCONNECT, SERVER_KICK, SERVER_KICK_BAN
    }

    private final Cause cause;

    public PlayerLeaveServerEvent(Player source, Server target, Cause cause) {
        super(source, target);
        this.cause = cause;
    }

    public Cause getCause() {
        return cause;
    }
}
