package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface BedrockProfile extends Profile {
    @Override default Type getType() {
        return Type.BEDROCK;
    }

    long getXUID();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder(Profile.super.serialize().getAsJsonObject())
                .property("xuid", getXUID())
                .build();
    }
}
