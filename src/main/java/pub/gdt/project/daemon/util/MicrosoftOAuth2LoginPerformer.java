package pub.gdt.project.daemon.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import pub.gdt.project.daemon.exception.MicrosoftOAuth2Exception;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Objects;

import static java.net.http.HttpResponse.BodySubscribers;

public final class MicrosoftOAuth2LoginPerformer {
    public static final String MINECRAFT_CLIENT_ID = "00000000402b5328";

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private static HttpRequest.BodyPublisher jsonBodyHandler(JsonElement jsonBody) {
        return HttpRequest.BodyPublishers.ofString(gson.toJson(jsonBody));
    }
    private static final HttpResponse.BodyHandler<JsonElement> jsonBodyHandler =
            responseInfo -> BodySubscribers.mapping(
                    BodySubscribers.ofString(Charset.defaultCharset()),
                    JsonParser::parseString
            );

    private static final JsonPointer POINTER_CODE2TOKEN_ACCESS_TOKEN = JsonPointer.newBuilder()
            .findByKey("access_token")
            .build();
    private static final JsonPointer POINTER_CODE2TOKEN_REFRESH_TOKEN = JsonPointer.newBuilder()
            .findByKey("refresh_token")
            .build();
    private static final JsonPointer POINTER_XBL_TOKEN = JsonPointer.newBuilder()
            .findByKey("Token")
            .build();
    private static final JsonPointer POINTER_XBL_UHS = JsonPointer.newBuilder()
            .findByKey("DisplayClaims")
            .findByKey("xui")
            .findByIndex(0)
            .findByKey("uhs")
            .build();
    private static final JsonPointer POINTER_XSTS_TOKEN = JsonPointer.newBuilder()
            .findByKey("Token")
            .build();
    private static final JsonPointer POINTER_XSTS_UHS = JsonPointer.newBuilder()
            .findByKey("DisplayClaims")
            .findByKey("xui")
            .findByIndex(0)
            .findByKey("uhs")
            .build();
    private static final JsonPointer POINTER_MINECRAFT_ACCESS_TOKEN = JsonPointer.newBuilder()
            .findByKey("access_token")
            .build();
    private static final JsonPointer POINTER_MINECRAFT_UUID = JsonPointer.newBuilder()
            .findByKey("id")
            .build();
    private static final JsonPointer POINTER_MINECRAFT_PLAYER_NAME = JsonPointer.newBuilder()
            .findByKey("name")
            .build();

    private final String authCode;
    private boolean success = false;
    private String playerName, uuid;

    public MicrosoftOAuth2LoginPerformer(String authCode) {
        this.authCode = authCode;
    }

    public void performLogin() throws MicrosoftOAuth2Exception {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            // Step 1: Code -> Token
            HttpRequest code2tokenRequest = HttpRequest.newBuilder(new URI("https://login.live.com/oauth20_token.srf"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "client_id=" + MINECRAFT_CLIENT_ID +
                            "&code=" + authCode +
                            "&grant_type=authorization_code" +
                            "&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf" +
                            "&scope=service::user.auth.xboxlive.com::MBI_SSL"))
                    .build();
            HttpResponse<JsonElement> code2tokenResponse = httpClient.send(code2tokenRequest, jsonBodyHandler);
            String accessToken, refreshToken;
            try {
                accessToken = POINTER_CODE2TOKEN_ACCESS_TOKEN.find(code2tokenResponse.body()).getAsString();
                refreshToken = POINTER_CODE2TOKEN_REFRESH_TOKEN.find(code2tokenResponse.body()).getAsString();
            } catch (NullPointerException | IllegalStateException e) {
                throw new MicrosoftOAuth2Exception("Invalid server response", e, MicrosoftOAuth2Exception.ErrorStep.CODE2TOKEN);
            }

            // Step 2: XBL Authentication
            HttpRequest xblAuthRequest = HttpRequest.newBuilder(new URI("https://user.auth.xboxlive.com/user/authenticate"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(jsonBodyHandler(new JsonObjectBuilder()
                            .object("Properties")
                                    .property("AuthMethod", "RPS")
                                    .property("SiteName", "user.auth.xboxlive.com")
                                    .property("RpsTicket", "t=" + accessToken)
                                    // Here I have to blame the tutorial on Minecraft Wiki (zh).
                                    // What I initially referred to is:
                                    // https://minecraft.fandom.com/zh/wiki/%E6%95%99%E7%A8%8B/%E7%BC%96%E5%86%99%E5%90%AF%E5%8A%A8%E5%99%A8
                                    // In this page, when it comes to XBL Authentication,
                                    // The value of node "/Properties/RpsTicket" appears to be "d=" + token.
                                    // When I did as it told, I got a 400 error.
                                    // After two days' research, I finally found the answer.
                                    // "RpsTicket", when the request is called in an Azure session, should start with "d=".
                                    // Otherwise, it should start with "t=".
                                    .close()
                            .property("RelyingParty", "http://auth.xboxlive.com")
                            .property("TokenType", "JWT")
                            .build()))
                    .build();
            HttpResponse<JsonElement> xblAuthResponse = httpClient.send(xblAuthRequest, jsonBodyHandler);
            String xblToken, xblUserHash;
            try {
                xblToken = POINTER_XBL_TOKEN.find(xblAuthResponse.body()).getAsString();
                xblUserHash = POINTER_XBL_UHS.find(xblAuthResponse.body()).getAsString();
            } catch (NullPointerException | IllegalStateException e) {
                throw new MicrosoftOAuth2Exception("Invalid server response", e, MicrosoftOAuth2Exception.ErrorStep.XBL_AUTH);
            }

            // Step 3: XSTS Authorization
            HttpRequest xstsAuthRequest = HttpRequest.newBuilder(new URI("https://xsts.auth.xboxlive.com/xsts/authorize"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(jsonBodyHandler(new JsonObjectBuilder()
                            .object("Properties")
                            .property("SandboxId", "RETAIL")
                                    .array("UserTokens")
                                            .string(xblToken)
                                            .closeAsSub()
                                    .close()
                            .property("RelyingParty", "rp://api.minecraftservices.com/")
                            .property("TokenType", "JWT")
                            .build()))
                    .build();
            HttpResponse<JsonElement> xstsAuthResponse = httpClient.send(xstsAuthRequest, jsonBodyHandler);
            // if (xstsAuthResponse.statusCode() == 404)
            //     throw new MicrosoftOAuth2Exception("404boundless not found", MicrosoftOAuth2Exception.ErrorStep.XSTS_AUTH);
            // Well, the error code 404 is never supposed to be returned.
            // In memory of 2023/6/24, the "404 Not Found" day.
            if (xstsAuthResponse.statusCode() == 401)
                throw new MicrosoftOAuth2Exception("Bad XBOX LIVE account status", MicrosoftOAuth2Exception.ErrorStep.XSTS_AUTH);
            String xstsToken, xstsUserHash;
            try {
                xstsToken = POINTER_XSTS_TOKEN.find(xstsAuthResponse.body()).getAsString();
                xstsUserHash = POINTER_XSTS_UHS.find(xstsAuthResponse.body()).getAsString();
            } catch (NullPointerException | IllegalStateException e) {
                throw new MicrosoftOAuth2Exception("Invalid server response", e, MicrosoftOAuth2Exception.ErrorStep.XSTS_AUTH);
            }

            assert Objects.equals(xblUserHash, xstsUserHash);

            // Step 4: Minecraft Authentication
            HttpRequest minecraftAuthRequest = HttpRequest.newBuilder(new URI("https://api.minecraftservices.com/authentication/login_with_xbox"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(jsonBodyHandler(new JsonObjectBuilder()
                            .property("identityToken", "XBL3.O x=" + xstsUserHash + ";" + xstsToken)
                            .build()))
                    .build();
            HttpResponse<JsonElement> minecraftAuthResponse = httpClient.send(minecraftAuthRequest, jsonBodyHandler);
            String minecraftAccessToken;
            try {
                minecraftAccessToken = POINTER_MINECRAFT_ACCESS_TOKEN.find(minecraftAuthResponse.body()).getAsString();
            } catch (NullPointerException | IllegalStateException e) {
                throw new MicrosoftOAuth2Exception("Invalid server response", e, MicrosoftOAuth2Exception.ErrorStep.MINECRAFT_AUTH);
            }

            // Step 5: Ownership Authorization & Account Info Query
            HttpRequest accountInfoRequest = HttpRequest.newBuilder(new URI("https://api.minecraftservices.com/minecraft/profile"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + minecraftAccessToken)
                    .GET()
                    .build();
            HttpResponse<JsonElement> accountInfoResponse = httpClient.send(accountInfoRequest, jsonBodyHandler);
            try {
                playerName = POINTER_MINECRAFT_PLAYER_NAME.find(accountInfoResponse.body()).getAsString();
                StringBuilder standardUUIDBuilder = new StringBuilder(POINTER_MINECRAFT_UUID.find(accountInfoResponse.body()).getAsString());
                // Convert a raw UUID into a standard one:
                // 00000000-000000000000000000000000
                //         ^ offset = 8
                // 00000000-0000-00000000000000000000
                //              ^ offset = 13
                // 00000000-0000-0000-0000000000000000
                //                   ^ offset = 18
                // 00000000-0000-0000-0000-000000000000 (Standard!)
                //                        ^ offset = 23
                standardUUIDBuilder.insert(8, '-');
                standardUUIDBuilder.insert(13, '-');
                standardUUIDBuilder.insert(18, '-');
                standardUUIDBuilder.insert(23, '-');
                uuid = standardUUIDBuilder.toString();
            } catch (NullPointerException | IllegalStateException e) {
                throw new MicrosoftOAuth2Exception("Invalid server response", e, MicrosoftOAuth2Exception.ErrorStep.OWNERSHIP_AUTH);
            }

            // Well we finally got here
            success = true;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPlayerName() {
        if (!success) throw new IllegalStateException("Login has not been performed");
        return playerName;
    }

    public String getUUID() {
        if (!success) throw new IllegalStateException("Login has not been performed");
        return uuid;
    }
}
