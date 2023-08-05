package pub.gdt.project.daemon.basic.apply;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.util.List;

public interface ServerForm extends JsonSerializable {
    String title();
    String description();
    List<Question> questions();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("title", title())
                .property("description", description())
                .array("questions")
                        .serializableObjects(questions())
                        .close()
                .build();
    }
}
