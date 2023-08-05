package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.Player;
import pub.gdt.project.daemon.basic.ServerApplyingSession;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.util.stream.Stream;

public interface ServerApplyingManager {
    enum Type {
        NOT_NEEDED("无需审核"),
        BY_PLAYER_META_ONLY("玩家信息"),
        BY_MESSAGE("验证消息"),
        BY_FORM("问卷");
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
    Type getApplyingType();
    ServerApplyingSession createSession(Player source, JsonElement message);
    Stream<ServerApplyingSession> getOngoingSessions();
    boolean hasApplyingSession(Player player);
    ServerApplyingSession getSession(Player source);

    /* default ServerApplyingSession createSession(Player source, JsonElement message) {
        try {
            JsonObject jsonObject = message.getAsJsonObject();
            String typeString = Objects.requireNonNull(jsonObject.get("type")).getAsString();
            Type type = Type.valueOf(typeString);
            if (type != getApplyingType()) throw new IllegalArgumentException("Unmatched applying type");
            return createSessionRaw(source, message);
        } catch (IllegalStateException | NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
    } */
}
