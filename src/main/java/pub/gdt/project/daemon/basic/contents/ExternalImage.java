package pub.gdt.project.daemon.basic.contents;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface ExternalImage extends JsonSerializable {
    String url();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("type", "ExternalImage")
                .property("url", url())
                .build();
    }
}
