package pub.gdt.project.daemon.basic.meta;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.basic.Profile;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface ServerMeta extends JsonSerializable {
    Profile.Type getAuthType();
    String getAddress();
    int getPort();
    MinecraftVersion getCoreVersion();
    VersionRange getVersionRange();
    boolean isModded();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("authType", getAuthType().name())
                .property("address", getAddress())
                .property("port", getPort())
                .serializable("coreVersion", getCoreVersion())
                .serializable("versionRange", getVersionRange())
                .property("isModded", isModded())
                .build();
    }
}
