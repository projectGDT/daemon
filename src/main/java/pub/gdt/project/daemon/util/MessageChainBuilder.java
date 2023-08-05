package pub.gdt.project.daemon.util;

import pub.gdt.project.daemon.bot.MessageChain;

public class MessageChainBuilder {
    private static final String AVATAR_SCHEMA = "https://q1.qlogo.cn/g?b=qq&nk={qq}&s=640";

    private final JsonArrayBuilder inner = new JsonArrayBuilder();

    public MessageChainBuilder plain(String text) {
        inner.subObject()
                .property("type", "Plain")
                .property("text", text)
                .close();
        return this;
    }

    public MessageChainBuilder enter() {
        return plain("\n");
    }

    public MessageChainBuilder line(String text) {
        return plain(text + "\n");
    }

    public MessageChainBuilder at(long qq) {
        inner.subObject()
                .property("type", "At")
                .property("target", qq)
                .close();
        return this;
    }

    public MessageChainBuilder image(String url) {
        inner.subObject()
                .property("type", "Image")
                .jsonNull("imageId")
                .property("url", url)
                .jsonNull("path")
                .jsonNull("base64")
                .close();
        return this;
    }

    public MessageChainBuilder avatar(long qq) {
        return image(AVATAR_SCHEMA.replace("{qq}", String.valueOf(qq)));
    }

    public MessageChain build() {
        return inner::build;
    }
}
