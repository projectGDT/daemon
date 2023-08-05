package pub.gdt.project.daemon.basic.apply;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface StringAnswerQuestion extends Question {
    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder(Question.super.serialize().getAsJsonObject())
                .property("type", "STRING_ANSWER")
                .build();
    }
}
