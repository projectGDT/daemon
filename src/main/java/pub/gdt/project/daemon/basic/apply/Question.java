package pub.gdt.project.daemon.basic.apply;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface Question extends JsonSerializable {
    int index();
    String content();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("index", index())
                .property("content", content())
                .build();
    }
}
