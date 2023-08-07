package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;

import java.util.stream.Stream;

public interface PlayerApplyingManager {
    enum Type {
        NOT_NEEDED, BY_PLAYER_META_ONLY, BY_MESSAGE, BY_FORM
    }
    Type getApplyingType();
    PlayerApplyingSession createSession(Player source, JsonElement message);
    Stream<PlayerApplyingSession> getOngoingSessions();
    boolean hasApplyingSession(Player player);
    PlayerApplyingSession getSession(Player source);

    /* default PlayerApplyingSession createSession(Player source, JsonElement message) {
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
