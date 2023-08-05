package pub.gdt.project.daemon.basic;

import pub.gdt.project.daemon.basic.Profile;

import java.util.stream.Stream;

public interface ProfileManager {

    // JavaProfile createOAuth2Profile(Profile.Type type, String authCode) throws OAuth2Exception;

    // VerificationCode createBedrockProfileSession(Consumer<BedrockProfile> successHandler, Runnable failureHandler);
    Profile getProfileById(long id);
    Profile getProfileByUUID(Profile.Type type, String uuid);
    Profile getProfileByXUID(long xuid);
    Stream<Profile> getProfileByPlayerName(String playerName);

    // boolean deleteProfile(Profile profile);
}
