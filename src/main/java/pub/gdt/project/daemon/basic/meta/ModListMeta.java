package pub.gdt.project.daemon.basic.meta;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.time.Instant;

public interface ModListMeta extends JsonSerializable {
    enum Type {
        MODRINTH_PACK
    }
    Type getType();
    Instant updatedAt();
    String getFullPackURL();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("type", getType().name())
                .property("updatedAt", updatedAt().getEpochSecond())
                .property("fullPackURL", getFullPackURL())
                .build();
    }
}
