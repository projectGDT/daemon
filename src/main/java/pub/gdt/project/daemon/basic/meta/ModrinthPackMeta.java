package pub.gdt.project.daemon.basic.meta;

public interface ModrinthPackMeta extends ModListMeta {
    @Override
    default Type getType() {
        return Type.MODRINTH_PACK;
    }
}
