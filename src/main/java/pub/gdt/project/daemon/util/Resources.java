package pub.gdt.project.daemon.util;

import java.io.IOException;
import java.io.InputStream;

public final class Resources {
    private static final ClassLoader classLoader = Resources.class.getClassLoader();

    public static InputStream asStream(String name) {
        return classLoader.getResourceAsStream(name);
    }

    public static String read(String name) {
        try (InputStream in = asStream(name)) {
            return new String(in.readAllBytes());
        } catch (IOException e) {
            return null;
        }
    }
}
