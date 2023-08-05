package pub.gdt.project.daemon.basic;

import pub.gdt.project.daemon.basic.Player;

public interface PlayerInstanceManager {
    boolean existsId(long id);
    Player getPlayerById(long id);
    boolean existsQQ(long qq);
    Player getPlayerByQQ(long qq);
    Player getPlayerByJavaProfileId(long javaProfileId);
    Player getPlayerByBedrockProfileId(long bedrockProfileId);
}
