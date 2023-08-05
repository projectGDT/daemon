package pub.gdt.project.daemon.basic.contents;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface MarkdownText extends JsonSerializable {
    byte[] content();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("type", "MarkdownText")
                .property("contentBase64", new String(content()))
                .build();
    }
}
