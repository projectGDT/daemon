package pub.gdt.project.daemon.test;

import com.google.gson.JsonElement;
import pub.gdt.project.daemon.exception.MicrosoftOAuth2Exception;
import pub.gdt.project.daemon.util.MicrosoftOAuth2LoginPerformer;

public class MicrosoftOAuth2Test {
    public static void main(String[] args) throws MicrosoftOAuth2Exception {
        long start = System.currentTimeMillis();
        String authCode = "M.C107_BAY.2.(your own code)";
        MicrosoftOAuth2LoginPerformer performer = new MicrosoftOAuth2LoginPerformer(authCode);
        JsonElement result = performer.performLogin();
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("Time elapsed: " + (end - start) + "ms");
    }
}
