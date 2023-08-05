package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;

import java.util.stream.Stream;

public interface ServerApplyingManager {
    enum Type {
        NOT_NEEDED, BY_PLAYER_META_ONLY, BY_MESSAGE, BY_FORM
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
