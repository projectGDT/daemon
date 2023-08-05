package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.io.IOException;
import java.util.Optional;

public interface Profile extends JsonSerializable {
    enum Type {
        // also in resources/sql/initialize.sql
        JAVA_MICROSOFT("Java 版 - 正版"),
        JAVA_LITTLE_SKIN("Java 版 - LittleSkin 外置"),

        // Unimplemented
        // JAVA_NIDE8("Java 版 - 统一通行证"),
        OFFLINE("Java 版 - 离线"),
        BEDROCK("基岩版 - XBOX LIVE");
        private final String value;
        Type(String value) { this.value = value; }
        public static JsonElement serializeTypeEnum() {
            return new JsonObjectBuilder()
                    .applyOperation(builder -> {
                        for (Type type : Type.values())
                            builder.property(type.name(), type.value);
                        return builder;
                    }).build();
        }
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
