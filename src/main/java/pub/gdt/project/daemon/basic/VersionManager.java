package pub.gdt.project.daemon.basic;

import pub.gdt.project.daemon.basic.meta.MinecraftVersion;
import pub.gdt.project.daemon.basic.meta.VersionRange;

public interface VersionManager {
    MinecraftVersion ofJava(int first, int second, int third);
    MinecraftVersion ofBedrock(int first, int second, int third);
    MinecraftVersion latestJava();
    MinecraftVersion latestBedrock();
    MinecraftVersion earliestGeyser();

    default VersionRange withViaVersion(MinecraftVersion lowerBound) {
        return VersionRange.between(lowerBound, latestJava());
    }

    default VersionRange withViaBackwards() {
        return VersionRange.between(ofJava(1, 9, 0), latestJava());
    }

    default VersionRange withViaRewind() {
        return VersionRange.between(ofJava(1, 7, 10), latestJava());
    }

    default VersionRange withGeyser() {
        return VersionRange.between(earliestGeyser(), latestBedrock());
    }
}
