package pub.gdt.project.daemon;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pub.gdt.project.daemon.event.EventChannel;
import pub.gdt.project.daemon.util.Resources;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

public class DaemonMain {
    private static final String RSA_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 256;
    private static final String DEFAULT_DATABASE_PROPERTIES_DIR = "database.properties";

    private static final Logger mainLogger = Logger.getLogger(DaemonMain.class.getName());

    private static Cipher decryptionCipher;
    private static String publicKeyString;

    private static Connection connection;

    @SuppressWarnings("all")
    public static void main(String[] args)
            throws SQLException, IOException, NoSuchAlgorithmException,
                   ClassNotFoundException, NoSuchPaddingException, InvalidKeyException {
        // Initialize RSA Decryption
        mainLogger.info("Initializing RSA Decryption...");
        Path encrytionDir = Path.of("encryption");
        Path publicKeyPath = encrytionDir.resolve("public.key"),
             privateKeyPath = encrytionDir.resolve("private.key");
        PublicKey rsaPublicKey;
        PrivateKey rsaPrivateKey;

        if (Files.notExists(encrytionDir)) Files.createDirectory(encrytionDir);
        if (Files.notExists(publicKeyPath) | Files.notExists(privateKeyPath)) {
            // Generate new key pair
            mainLogger.info("public.key & private.key not found. Generating...");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            rsaPublicKey = keyPair.getPublic();
            rsaPrivateKey = keyPair.getPrivate();
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(publicKeyPath))) {
                out.writeObject(rsaPublicKey);
            }
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(privateKeyPath))) {
                out.writeObject(rsaPrivateKey);
            }
        } else {
            // Read key pair from file
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(publicKeyPath))) {
                rsaPublicKey = (PublicKey) in.readObject();
            }
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(privateKeyPath))) {
                rsaPrivateKey = (PrivateKey) in.readObject();
            }
        }

        publicKeyString = rsaPublicKey.toString();

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
}
