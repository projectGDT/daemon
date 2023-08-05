package pub.gdt.project.daemon.event.player;

import pub.gdt.project.daemon.basic.Player;
import pub.gdt.project.daemon.basic.Server;

public final class PlayerJoinServerEvent extends PlayerServerEvent {
    public PlayerJoinServerEvent(Player source, Server target) {
        super(source, target);
    }
}
