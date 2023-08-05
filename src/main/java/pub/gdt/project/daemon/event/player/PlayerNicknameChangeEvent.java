package pub.gdt.project.daemon.event.player;

import pub.gdt.project.daemon.basic.Player;

public class PlayerNicknameChangeEvent extends PlayerEvent {
    private final String newNickname;

    public PlayerNicknameChangeEvent(Player source, String newNickname) {
        super(source);
        this.newNickname = newNickname;
    }

    public String getNewNickname() {
        return newNickname;
    }
}
