package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.contents.MarkdownText;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

public interface Player extends JsonSerializable {
    long getId();
    void setQQ(long qq);
    long getQQ();
    void setNickname(String nickname);
    String getNickname();
    void setDescription(MarkdownText description);
    MarkdownText getDescription();
    LocalDate registeredOn();
    boolean addJavaProfile(JavaProfile profile);
    boolean removeJavaProfile(JavaProfile profile);
    Stream<JavaProfile> getJavaProfiles();
    default Stream<JavaProfile> getJavaProfileByType(Profile.Type type) {
        return getJavaProfiles().filter(javaProfile -> javaProfile.getType() == type);
    }
    boolean setBedrockProfile(BedrockProfile profile);
    BedrockProfile getBedrockProfile();
    default Stream<Profile> getProfiles() {
        return Stream.concat(getJavaProfiles(), Stream.of(getBedrockProfile()));
    }
    Stream<Server> getOwnedServers();
    Stream<Server> getManagedServers();
    Stream<Server> getInvolvedServers();

    default Stream<Server> getCurrentServers() {
        return getProfiles()
                .map(Profile::getCurrentServer)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    default boolean isOnline() {
        return getProfiles().anyMatch(profile -> profile.getCurrentServer().isPresent());
    }

    Stream<ServerApplyingSession> getApplyingSessions();

    default JsonElement serializeSimple() {
        return new JsonObjectBuilder()
                .property("id", getId())
                .property("nickname", getNickname())
                .build();
    }

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder(serializeSimple().getAsJsonObject())
                .serializable("description", getDescription())
                .property("registeredOn", registeredOn().toString())
                .array("profiles")
                        .serializableObjects(getProfiles().toList())
                        .close()
                .property("isOnline", isOnline())
                .build();
    }

    default JsonElement serializeSimpleByServer(Server server) {
        return new JsonObjectBuilder()
                .property("id", getId())
                .property("nickname", server.getServerSpecifiedNickname(this))
                .property("isManager", server.isManager(this))
                .build();
    }
}
