package pub.gdt.project.daemon;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.rtner.security.auth.spi.SimplePBKDF2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.gdt.project.daemon.event.EventChannel;
import pub.gdt.project.daemon.util.Resources;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Properties;

public class DaemonMain {
    private static final String DEFAULT_DATABASE_PROPERTIES_DIR = "database.properties";

    private static final Logger mainLogger = LoggerFactory.getLogger(DaemonMain.class);

    // RSA Constants
    private static final String RSA_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 512;
    private static Cipher decryptionCipher;
    private static String publicKeyString;

    // PBKDF2 Constants
    private static final SimplePBKDF2 pbkdf2 = new SimplePBKDF2();

    private static Connection connection;

    @SuppressWarnings("all")
    public static void main(String[] args)
            throws SQLException, IOException, NoSuchAlgorithmException,
                   ClassNotFoundException, NoSuchPaddingException, InvalidKeyException {
        // Initialize RSA Decryption
        mainLogger.info("Initializing RSA Decryption...");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey rsaPublicKey = keyPair.getPublic();
        PrivateKey rsaPrivateKey = keyPair.getPrivate();
        publicKeyString = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
        mainLogger.info("RSA PublicKey prepared: {}", publicKeyString);

        decryptionCipher = Cipher.getInstance(RSA_ALGORITHM);
        decryptionCipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);

        // Initialize SQL Connection
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Path.of(System.getProperty("gdt.databasePropertiesDir", DEFAULT_DATABASE_PROPERTIES_DIR))));
        HikariConfig config = new HikariConfig(properties);
        HikariDataSource dataSource = new HikariDataSource(config);
        connection = dataSource.getConnection();

        // Handle keyboard input ^C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Stop accepting HTTP requests & WebSocket connections

            // Stop accepting events
            EventChannel.stopAccepting();

            // Broadcast server closing

            // Wait all queries and updates to end

            // Close SQL connection
            try {
                connection.close();
            } catch (SQLException ignored) {}
            dataSource.close();

            // Goodbye
        }));

        // Check and initialize database
        try (Statement statement = connection.createStatement()) {
            statement.execute(Resources.read("sql/initialize.sql"));
        }
    }

    public static Connection getDatabaseConnection() {
        return connection;
    }

    public static byte[] decryptRSA(byte[] input) {
        try {
            return decryptionCipher.doFinal(input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRSAPublicKey() {
        return publicKeyString;
    }

    public static String deriveKeyFormatted(String raw) {
        return pbkdf2.deriveKeyFormatted(raw);
    }

    public static boolean verifyKeyFormatted(String formatted, String raw) {
        return pbkdf2.verifyKeyFormatted(formatted, raw);
    }
}
