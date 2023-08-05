package pub.gdt.project.daemon.basic.meta;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import pub.gdt.project.daemon.basic.JsonSerializable;
import pub.gdt.project.daemon.util.JsonObjectBuilder;

public final class MinecraftVersion implements Comparable<MinecraftVersion>, JsonSerializable {
    public enum Edition {
        JAVA, BEDROCK
    }

    final Edition edition;
    private final int first, second, third;
    public MinecraftVersion(Edition edition, int first, int second, int third) {
        this.edition = edition;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public String toString() {
        return edition.toString() + " " + first + '.' + second + '.' + third;
    }

    public int compareTo(@NotNull MinecraftVersion other) {
        return this == other ? 0 :
                this.first != other.first ?
                        Integer.compare(this.first, other.first) :
                        this.second != other.second ?
                                Integer.compare(this.second, other.second) :
                                Integer.compare(this.third, other.third);
    }

    public JsonElement serialize() {
        return new JsonObjectBuilder()
                .property("edition", edition.name())
                .array("digits")
                        .number(first)
                        .number(second)
                        .number(third)
                        .close()
                .build();
    }
}
