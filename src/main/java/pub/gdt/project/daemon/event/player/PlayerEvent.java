package pub.gdt.project.daemon.event.player;

import pub.gdt.project.daemon.basic.Player;
import pub.gdt.project.daemon.event.Event;

public abstract class PlayerEvent extends Event<Player> {
    public PlayerEvent(Player source) {
        super(source);
    }
}
