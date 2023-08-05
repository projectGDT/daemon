package pub.gdt.project.daemon.basic.meta;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface JavaServerMeta extends ServerMeta {
    ModListMeta getModListMeta();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder(ServerMeta.super.serialize().getAsJsonObject())
                .applyOperation(builder -> isModded() ?
                        builder.serializable("modList", getModListMeta()) :
                        builder)
                .build();
    }
}
