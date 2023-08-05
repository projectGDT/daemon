package pub.gdt.project.daemon.basic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.meta.JavaServerMeta;
import pub.gdt.project.daemon.basic.meta.ServerDescription;
import pub.gdt.project.daemon.basic.meta.ServerMeta;
import pub.gdt.project.daemon.basic.meta.ServerTag;
import pub.gdt.project.daemon.util.JsonArrayBuilder;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.util.stream.Stream;

public interface Server extends JsonSerializable {
    // Basic Data
    long getId();
    void setGroupId(long groupId);
    long getGroupId();
    void setName(String name);
    String getName();

    // Metadata
    void setDescription(ServerDescription description);
    ServerDescription getDescription();
    boolean supportsJava();
    JavaServerMeta getJavaServerMeta();
    OfflinePlayerManager getOfflinePlayerManager();
    boolean supportsBedrock();
    ServerMeta getBedrockServerMeta();

    // Players
    int getPlayerCount();
    boolean addPlayer(Player player);
    boolean removePlayer(Player player);
    Stream<Player> getPlayers();
    String getServerSpecifiedNickname(Player player);
    boolean setOwner(Player owner);
    Player getOwner();
    boolean addManager(Player manager);
    boolean removeManager(Player manager);
    boolean isManager(Player player);
    default Stream<Player> getManagers() {
        return getPlayers().filter(this::isManager);
    }

    // Tags
    void setTags(Stream<ServerTag> tags);
    Stream<ServerTag> getTags();

    // Applying
    default ServerApplyingManager.Type getApplyingType() {
        return getApplyingManager().getApplyingType();
    }
    ServerApplyingManager getApplyingManager();

    // Status
    ServerStatusManager getStatusManager();

    default JsonElement serializeSimple() {
        return new JsonObjectBuilder()
                .property("id", getId())
                .property("groupId", getGroupId())
                .property("name", getName())
                .array("tags")
                        .numbers(getTags().map(ServerTag::getId).toList())
                        .close()
                .property("playerCount", getPlayerCount())
                .build();
    }

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder(serializeSimple().getAsJsonObject())
                .serializable("description", getDescription())
                .applyOperation(builder -> supportsJava() ?
                        builder.serializable("javaServerMeta", getJavaServerMeta()) :
                        builder)
                .applyOperation(builder -> supportsBedrock() ?
                        builder.serializable("bedrockServerMeta", getBedrockServerMeta()) :
                        builder)
                .element("owner", getOwner().serializeSimple())
                .property("applyingType", getApplyingType().name())
                .build();
    }

    default JsonArray serializeFullPlayerList() {
        return new JsonArrayBuilder()
                .applyOperation(builder -> builder.elements(getPlayers()
                        .map(player -> player.serializeSimpleByServer(this))
                        .toList()))
                .build();
    }
}
