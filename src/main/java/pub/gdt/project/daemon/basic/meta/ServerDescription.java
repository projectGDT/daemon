package pub.gdt.project.daemon.basic.meta;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.basic.contents.ExternalImage;
import pub.gdt.project.daemon.basic.contents.MarkdownText;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.util.stream.Stream;

public interface ServerDescription extends JsonSerializable {
    Stream<ExternalImage> getCoverImages();
    MarkdownText getText();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder()
                .array("coverImages")
                        .serializableObjects(getCoverImages().toList())
                        .close()
                .serializable("text", getText())
                .build();
    }
}
