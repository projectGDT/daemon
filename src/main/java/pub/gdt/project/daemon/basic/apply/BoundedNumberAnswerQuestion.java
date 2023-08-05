package pub.gdt.project.daemon.basic.apply;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public interface BoundedNumberAnswerQuestion extends NumberAnswerQuestion {
    int lowerBound();
    int upperBound();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder(NumberAnswerQuestion.super.serialize().getAsJsonObject())
                .property("lowerBound", lowerBound())
                .property("upperBound", upperBound())
                .build();
    }
}
