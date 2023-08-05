package pub.gdt.project.daemon.exception;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public class OAuth2Exception extends Exception implements JsonSerializable {

    private final String authServer;
    public OAuth2Exception(String authServer, String message) {
        super(message);
        this.authServer = authServer;
    }

    public JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("type", "OAuth2Exception")
                .property("authServer", authServer)
                .property("message", getMessage())
                .build();
    }
}
