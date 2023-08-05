package pub.gdt.project.daemon.basic;

import pub.gdt.project.daemon.basic.Player;

public interface OfflinePlayerManager {
    boolean existsProfileName();
    Player getByProfileName(String name);
    String getPlayerName(Player player);
}
