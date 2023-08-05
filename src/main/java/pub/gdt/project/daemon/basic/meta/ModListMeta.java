package pub.gdt.project.daemon.basic.meta;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.time.Instant;

public interface ModListMeta extends JsonSerializable {
    enum Type {
        MODRINTH_PACK("Modrinth 整合包");
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
