package pub.gdt.project.daemon.basic.apply;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.util.List;

public interface ChoiceQuestion extends Question {
    List<String> choices();
    boolean hasOthersBlank();
    boolean allowsMultipleChoice();
    List<Integer> downstreamIndexes();

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder(Question.super.serialize().getAsJsonObject())
                .property("type", "CHOICE")
                .array("choices")
                        .strings(choices())
                        .close()
                .property("hasOthersBlank", hasOthersBlank())
                .property("allowsMultipleChoice", allowsMultipleChoice())
                .array("downstreamIndexes")
                        .numbers(downstreamIndexes())
                        .close()
                .build();
    }
}
