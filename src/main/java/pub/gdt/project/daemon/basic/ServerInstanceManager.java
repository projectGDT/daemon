package pub.gdt.project.daemon.basic;

import pub.gdt.project.daemon.basic.meta.ServerTag;

import java.util.stream.Stream;

public interface ServerInstanceManager {
    Stream<Server> getAllServers();
    Server getServerById(long id);
    Stream<ServerTag> availableTags();
    ServerTag getTagById(short id);
    Stream<Server> getServerByTags(Stream<ServerTag> tags);
}
