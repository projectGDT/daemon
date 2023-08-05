package pub.gdt.project.daemon.basic.apply;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface NumberAnswerQuestion extends Question {
    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder(Question.super.serialize().getAsJsonObject())
                .property("type", "NUMBER_ANSWER")
                .build();
    }
}
