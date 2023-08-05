package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface JavaProfile extends Profile {
    String getUUID();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder(Profile.super.serialize().getAsJsonObject())
                .property("uuid", getUUID())
                .build();
    }
}
