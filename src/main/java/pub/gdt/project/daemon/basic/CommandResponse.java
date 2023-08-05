package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface CommandResponse extends JsonSerializable {
    boolean isSuccessful();
    String getResponseText();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("isSuccessful", isSuccessful())
                .property("responseText", getResponseText())
                .build();
    }
}
