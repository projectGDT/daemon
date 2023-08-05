package pub.gdt.project.daemon.basic;

import pub.gdt.project.daemon.basic.Player;

import java.io.IOException;
import java.util.stream.Stream;

public interface ServerStatusManager {
    boolean isAvailable();
    double getSystemCPULoad() throws IOException;
    double getSystemMemoryLoad() throws IOException;
    int onlinePlayerCount() throws IOException;
    Stream<Player> getOnlinePlayers() throws IOException;
    boolean kickPlayer(Player player) throws IOException;
    // CommandResponse issueCommand(String command) throws IOException;
    boolean stop() throws IOException;
}
