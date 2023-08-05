package pub.gdt.project.daemon.event.player;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.Player;
import pub.gdt.project.daemon.basic.Server;

public final class PlayerApplyToServerEvent extends PlayerServerEvent {
    private final JsonElement load;

    public PlayerApplyToServerEvent(Player source, Server target, JsonElement load) {
        super(source, target);
        this.load = load;
    }

    public JsonElement getLoad() {
        return load;
    }
}
