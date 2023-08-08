package pub.gdt.project.daemon.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.util.Base64;

public class RSAKeyTest {
    private static final Logger logger = LoggerFactory.getLogger(RSAKeyTest.class);

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey rsaPublicKey = keyPair.getPublic();
        PrivateKey rsaPrivateKey = keyPair.getPrivate();
        String publicKeyString = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded());
        logger.info("RSA PublicKey prepared: {}", publicKeyString);
        logger.info("RSA PrivateKey prepared: {}", privateKeyString);
    }
}
