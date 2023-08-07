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
    public static final String CODE2TOKEN_URL = "https://login.live.com/oauth20_token.srf";
    public static final String XBL_AUTH_URL = "https://user.auth.xboxlive.com/user/authenticate";
    public static final String XBL_RELYING_PARTY = "http://auth.xboxlive.com";
    public static final String XSTS_AUTH_URL = "https://xsts.auth.xboxlive.com/xsts/authorize";
    public static final String MINECRAFT_JE_RELYING_PARTY = "rp://api.minecraftservices.com/";
    public static final String MINECRAFT_JE_AUTH_LINK = "https://api.minecraftservices.com/authentication/login_with_xbox";;
    public static final String MINECRAFT_JE_SERVICE_PROFILE = "https://api.minecraftservices.com/minecraft/profile";

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
    private final HttpClient httpClient;

    public MicrosoftOAuth2LoginPerformer(String authCode) {
        this.authCode = authCode;
        httpClient = HttpClient.newHttpClient();
    }

    public JsonElement performLogin() throws MicrosoftOAuth2Exception {
        try {
            // Step 1: Code -> Token
            String accessToken = authCode2AccessToken(authCode);

            // Step 2: XBL Authentication
            XboxAuthResponse xblResponse = performXBLAuth(accessToken);

            // Step 3: XSTS Authorization
            XboxAuthResponse xstsResponse = performXSTSAuth(xblResponse.token());

            assert Objects.equals(xblResponse.userHash(), xstsResponse.userHash());

            // Step 4: Minecraft: Java Edition Authentication
            String minecraftAccessToken = getMinecraftAccessToken(xstsResponse);

            // Step 5: Java Edition Ownership Authorization & Account Info Query
            JsonElement minecraftJEAccountInfo = getMinecraftJEProfile(minecraftAccessToken);
            try {
                POINTER_MINECRAFT_PLAYER_NAME.find(minecraftJEAccountInfo).getAsString(); // validate the server response
                return minecraftJEAccountInfo;
            } catch (NullPointerException | IllegalStateException e) {
                throw new MicrosoftOAuth2Exception("Invalid server response", e, MicrosoftOAuth2Exception.ErrorStep.OWNERSHIP_AUTH);
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String authCode2AccessToken(String authCode) throws URISyntaxException, IOException, InterruptedException, MicrosoftOAuth2Exception {
        HttpRequest code2tokenRequest = HttpRequest.newBuilder(new URI(CODE2TOKEN_URL))
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
        try {
            return POINTER_CODE2TOKEN_ACCESS_TOKEN.find(code2tokenResponse.body()).getAsString();
        } catch (NullPointerException | IllegalStateException e) {
            throw new MicrosoftOAuth2Exception("Invalid server response", e, MicrosoftOAuth2Exception.ErrorStep.CODE2TOKEN);
        }
    }

    private XboxAuthResponse performXBLAuth(String accessToken) throws URISyntaxException, IOException, InterruptedException, MicrosoftOAuth2Exception {
        HttpRequest xblAuthRequest = HttpRequest.newBuilder(new URI(XBL_AUTH_URL))
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
                        .property("RelyingParty", XBL_RELYING_PARTY)
                        .property("TokenType", "JWT")
                        .build()))
                .build();
        HttpResponse<JsonElement> xblAuthResponse = httpClient.send(xblAuthRequest, jsonBodyHandler);
        try {
            String xblToken = POINTER_XBL_TOKEN.find(xblAuthResponse.body()).getAsString();
            String xblUserHash = POINTER_XBL_UHS.find(xblAuthResponse.body()).getAsString();
            return new XboxAuthResponse() {
                public String token() { return xblToken; }
                public String userHash() { return xblUserHash; }
            };
        } catch (NullPointerException | IllegalStateException e) {
            throw new MicrosoftOAuth2Exception("Invalid server response", e, MicrosoftOAuth2Exception.ErrorStep.XBL_AUTH);
        }
    }

    private XboxAuthResponse performXSTSAuth(String xblToken) throws URISyntaxException, IOException, InterruptedException, MicrosoftOAuth2Exception {
        HttpRequest xstsAuthRequest = HttpRequest.newBuilder(new URI(XSTS_AUTH_URL))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(jsonBodyHandler(new JsonObjectBuilder()
                        .object("Properties")
                                .property("SandboxId", "RETAIL")
                                .array("UserTokens")
                                        .string(xblToken)
                                        .closeAsSub()
                                .close()
                        .property("RelyingParty", MINECRAFT_JE_RELYING_PARTY)
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
        try {
            String xstsToken = POINTER_XSTS_TOKEN.find(xstsAuthResponse.body()).getAsString();
            String xstsUserHash = POINTER_XSTS_UHS.find(xstsAuthResponse.body()).getAsString();
            return new XboxAuthResponse() {
                public String token() { return xstsToken; }
                public String userHash() { return xstsUserHash; }
            };
        } catch (NullPointerException | IllegalStateException e) {
            throw new MicrosoftOAuth2Exception("Invalid server response", e, MicrosoftOAuth2Exception.ErrorStep.XSTS_AUTH);
        }
    }

    private String getMinecraftAccessToken(XboxAuthResponse xstsResponse) throws URISyntaxException, IOException, InterruptedException, MicrosoftOAuth2Exception {
        HttpRequest minecraftAuthRequest = HttpRequest.newBuilder(new URI(MINECRAFT_JE_AUTH_LINK))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(jsonBodyHandler(new JsonObjectBuilder()
                        .property("identityToken", "XBL3.O x=" + xstsResponse.userHash() + ";" + xstsResponse.token())
                        .build()))
                .build();
        HttpResponse<JsonElement> minecraftAuthResponse = httpClient.send(minecraftAuthRequest, jsonBodyHandler);
        try {
            return POINTER_MINECRAFT_ACCESS_TOKEN.find(minecraftAuthResponse.body()).getAsString();
        } catch (NullPointerException | IllegalStateException e) {
            throw new MicrosoftOAuth2Exception("Invalid server response", e, MicrosoftOAuth2Exception.ErrorStep.MINECRAFT_AUTH);
        }
    }

    private JsonElement getMinecraftJEProfile(String minecraftAccessToken) throws URISyntaxException, IOException, InterruptedException {
         return httpClient.send(
                 HttpRequest.newBuilder(new URI(MINECRAFT_JE_SERVICE_PROFILE))
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", "Bearer " + minecraftAccessToken)
                        .GET()
                        .build(),
                 jsonBodyHandler
         ).body();
    }

    private interface XboxAuthResponse {
        String token();
        String userHash();
    }

    @SuppressWarnings("all")
    public String rawUUID2Formatted(String rawUUID) {
        if (rawUUID.getBytes().length != 32)
            throw new IllegalArgumentException("Invalid UUID");
        StringBuilder result = new StringBuilder(rawUUID);
        // Convert a raw UUID into a formatted one:
        // 00000000-000000000000000000000000
        //         ^ offset = 8
        // 00000000-0000-00000000000000000000
        //              ^ offset = 13
        // 00000000-0000-0000-0000000000000000
        //                   ^ offset = 18
        // 00000000-0000-0000-0000-000000000000
        //                        ^ offset = 23
        result.insert(8, '-');
        result.insert(13, '-');
        result.insert(18, '-');
        result.insert(23, '-');
        return result.toString();
    }
}
