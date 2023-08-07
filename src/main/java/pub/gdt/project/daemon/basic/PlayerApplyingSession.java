package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

import java.time.Instant;
import java.util.Optional;

public interface PlayerApplyingSession extends JsonSerializable {
    Instant createdAt();
    Player getSource();
    Server getTarget();
    JsonElement getLoad();
    void accept(String reason);
    void reject(String reason);
    Optional<Result> getResult();
    interface Result extends JsonSerializable {
        boolean isAccepted();
        String getReason();
    }

    @Override
    default JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("createdAt", createdAt().getEpochSecond())
                .property("source", getSource().getId())
                .property("target", getTarget().getId())
                .element("load", getLoad())
                .applyOperation(builder -> getResult().isPresent() ?
                        builder.serializable("result", getResult().get()) :
                        builder)
                .build();
    }
}
