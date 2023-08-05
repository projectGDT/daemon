package pub.gdt.project.daemon.impl;

import pub.gdt.project.daemon.basic.Profile;
import pub.gdt.project.daemon.basic.Server;

import java.io.IOException;
import java.util.Optional;

public abstract class AbstractProfile implements Profile {

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String currentPlayerName() {
        return null;
    }

    @Override
    public Optional<Server> getCurrentServer() {
        return Optional.empty();
    }

    @Override
    public boolean kickFromServer() throws IOException {
        return false;
    }
}
