package pub.gdt.project.daemon.event.player;

import pub.gdt.project.daemon.basic.Player;
import pub.gdt.project.daemon.basic.Server;

public abstract class PlayerServerEvent extends PlayerEvent {
    private final Server target;
    public PlayerServerEvent(Player source, Server target) {
        super(source);
        this.target = target;
    }

    public Server getTarget() {
        return target;
    }
}
