package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.io.IOException;
import java.util.Optional;

public interface Profile extends JsonSerializable {
    enum Type {
        JAVA_MICROSOFT, JAVA_LITTLE_SKIN, OFFLINE, BEDROCK
    }

    Type getType();

    long getId();
    String currentPlayerName();
    Optional<Server> getCurrentServer();
    boolean kickFromServer() throws IOException;

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("id", getId())
                .property("type", getType().name())
                .property("playerName", currentPlayerName())
                .applyOperation(builder -> {
                    Optional<Server> result = getCurrentServer();
                    return result.isPresent() ?
                            builder.element("currentServer", result.get().serializeSimple()) :
                            builder;
                }).build();
    }
}
