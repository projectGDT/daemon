package pub.gdt.project.daemon.impl;

import pub.gdt.project.daemon.basic.Profile;
import pub.gdt.project.daemon.basic.ProfileManager;

import java.util.stream.Stream;

public class ProfileManagerImpl implements ProfileManager {
    @Override
    public Profile getProfileById(long id) {
        return null;
    }

    @Override
    public Profile getProfileByUUID(Profile.Type type, String uuid) {
        return null;
    }

    @Override
    public Profile getProfileByXUID(long xuid) {
        return null;
    }

    @Override
    public Stream<Profile> getProfileByPlayerName(String playerName) {
        return null;
    }
}
