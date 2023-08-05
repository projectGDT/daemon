package pub.gdt.project.daemon.util;

import com.google.gson.JsonElement;

import java.util.function.Function;

public class JsonPointer {
    private Function<JsonElement, JsonElement> elementFinder;

    public JsonElement find(JsonElement root) {
        return elementFinder.apply(root);
    }

    public static class Builder {
        private final JsonPointer result;

        public Builder() {
            result = new JsonPointer();
            result.elementFinder = element -> element;
        }

        public Builder findByKey(String key) {
            result.elementFinder = result.elementFinder.andThen(
                    element -> element.getAsJsonObject().get(key)
            );
            return this;
        }

        public Builder findByIndex(int index) {
            result.elementFinder = result.elementFinder.andThen(
                    element -> element.getAsJsonArray().get(index)
            );
            return this;
        }

        public JsonPointer build() {
            return result;
        }
    }
}
