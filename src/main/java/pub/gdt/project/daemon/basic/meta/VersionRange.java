package pub.gdt.project.daemon.basic.meta;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public final class VersionRange implements JsonSerializable {
    private final MinecraftVersion lowerBound, upperBound;
    private VersionRange(@NotNull MinecraftVersion lowerBound, @NotNull MinecraftVersion upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public JsonElement serialize() {
        return new JsonObjectBuilder()
                .serializable("lowerBound", lowerBound)
                .serializable("upperBound", upperBound)
                .build();
    }

    public boolean isSingleton() {
        return lowerBound == upperBound;
    }

    public static VersionRange between(@NotNull MinecraftVersion lowerBound, @NotNull MinecraftVersion upperBound) {
        if (lowerBound.edition != upperBound.edition)
            throw new IllegalArgumentException("Editions do not match!");
        if (lowerBound.compareTo(upperBound) > 0)
            throw new IllegalArgumentException("The lower bound must be earlier or equal to the upper bound!");
        return new VersionRange(lowerBound, upperBound);
    }

    public static VersionRange singleton(@NotNull MinecraftVersion minecraftVersion) {
        return between(minecraftVersion, minecraftVersion);
    }
}
