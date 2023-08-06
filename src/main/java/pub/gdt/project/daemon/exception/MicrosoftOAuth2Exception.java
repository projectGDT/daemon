package pub.gdt.project.daemon.exception;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public final class MicrosoftOAuth2Exception extends Exception implements JsonSerializable {
    public enum ErrorStep {
        CODE2TOKEN, XBL_AUTH, XSTS_AUTH, MINECRAFT_AUTH, OWNERSHIP_AUTH
    }
    private final ErrorStep errorStep;

    public MicrosoftOAuth2Exception(String message, ErrorStep errorStep) {
        super(message);
        this.errorStep = errorStep;
    }

    public MicrosoftOAuth2Exception(String message, Exception cause, ErrorStep errorStep) {
        super(message, cause);
        this.errorStep = errorStep;
    }

    public JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("type", "MicrosoftOAuth2Exception")
                .property("authType", errorStep.name())
                .property("message", getMessage())
                .build();
    }
}
