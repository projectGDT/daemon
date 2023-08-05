package pub.gdt.project.daemon.basic.apply;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface FixedAnswerQuestion extends Question {
    String answer();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder(Question.super.serialize().getAsJsonObject())
                .property("type", "FIXED_ANSWER")
                .property("answer", answer())
                .build();
    }
}
