package pub.gdt.project.daemon.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import pub.gdt.project.daemon.basic.JsonSerializable;

import java.util.function.UnaryOperator;

public class JsonObjectBuilder {
    private final JsonObject result;
    private final JsonObjectBuilder parent;
    public JsonObjectBuilder() {
        result = new JsonObject();
        parent = null;
    }

    public JsonObjectBuilder(JsonObject original) {
        result = original;
        parent = null;
    }

    public JsonObjectBuilder property(String key, Number value) {
        result.addProperty(key, value);
        return this;
    }

    public JsonObjectBuilder property(String key, Boolean value) {
        result.addProperty(key, value);
        return this;
    }

    public JsonObjectBuilder property(String key, Character value) {
        result.addProperty(key, value);
        return this;
    }

    public JsonObjectBuilder property(String key, String value) {
        result.addProperty(key, value);
        return this;
    }

    public JsonObjectBuilder element(String key, JsonElement value) {
        result.add(key, value);
        return this;
    }

    public JsonObjectBuilder jsonNull(String key) {
        return element(key, JsonNull.INSTANCE);
    }

    public JsonObjectBuilder serializable(String key, JsonSerializable serializableObject) {
        return element(key, serializableObject.serialize());
    }

    public SubObjectBuilder object(String key) {
        return new SubObjectBuilder(key);
    }

    public SubArrayBuilder array(String key) {
        return new SubArrayBuilder(key);
    }

    @SuppressWarnings("unchecked")
    public <T extends JsonObjectBuilder> T applyOperation(UnaryOperator<T> operator) {
        return operator.apply((T) this);
    }

    public JsonObject build() {
        return result;
    }

    public class SubObjectBuilder extends JsonObjectBuilder {
        private final String key;
        private SubObjectBuilder(String key) {
            this.key = key;
        }

        public JsonObjectBuilder.SubObjectBuilder property(String key, Number value) {
            return (JsonObjectBuilder.SubObjectBuilder) super.property(key, value);
        }

        public JsonObjectBuilder.SubObjectBuilder property(String key, Boolean value) {
            return (JsonObjectBuilder.SubObjectBuilder) super.property(key, value);
        }

        public JsonObjectBuilder.SubObjectBuilder property(String key, Character value) {
            return (JsonObjectBuilder.SubObjectBuilder) super.property(key, value);
        }

        public JsonObjectBuilder.SubObjectBuilder property(String key, String value) {
            return (JsonObjectBuilder.SubObjectBuilder) super.property(key, value);
        }

        public JsonObjectBuilder.SubObjectBuilder element(String key, JsonElement value) {
            return (JsonObjectBuilder.SubObjectBuilder) super.element(key, value);
        }

        public JsonObjectBuilder.SubObjectBuilder jsonNull(String key) {
            return (JsonObjectBuilder.SubObjectBuilder) super.jsonNull(key);
        }

        public JsonObjectBuilder.SubObjectBuilder serializable(String key, JsonSerializable serializableObject) {
            return element(key, serializableObject.serialize());
        }

        public JsonObjectBuilder close() {
            JsonObjectBuilder.this.element(key, JsonObjectBuilder.SubObjectBuilder.this.build());
            return JsonObjectBuilder.this;
        }

        public JsonObjectBuilder.SubObjectBuilder closeAsSub() {
            return (JsonObjectBuilder.SubObjectBuilder) close();
        }
    }

    public class SubArrayBuilder extends JsonArrayBuilder {
        private final String key;
        SubArrayBuilder(String key) {
            this.key = key;
        }

        public JsonObjectBuilder.SubArrayBuilder number(Number entry) {
            return (JsonObjectBuilder.SubArrayBuilder) super.number(entry);
        }

        public JsonObjectBuilder.SubArrayBuilder numbers(Iterable<? extends Number> entries) {
            return (JsonObjectBuilder.SubArrayBuilder) super.numbers(entries);
        }

        public JsonObjectBuilder.SubArrayBuilder bool(Boolean entry) {
            return (JsonObjectBuilder.SubArrayBuilder) super.bool(entry);
        }

        public JsonObjectBuilder.SubArrayBuilder character(Character entry) {
            return (JsonObjectBuilder.SubArrayBuilder) super.character(entry);
        }

        public JsonObjectBuilder.SubArrayBuilder characters(Iterable<Character> entries) {
            return (JsonObjectBuilder.SubArrayBuilder) super.characters(entries);
        }

        public JsonObjectBuilder.SubArrayBuilder string(String entry) {
            return (JsonObjectBuilder.SubArrayBuilder) super.string(entry);
        }

        public JsonObjectBuilder.SubArrayBuilder strings(Iterable<String> entries) {
            return (JsonObjectBuilder.SubArrayBuilder) super.strings(entries);
        }

        public JsonObjectBuilder.SubArrayBuilder element(JsonElement entry) {
            return (JsonObjectBuilder.SubArrayBuilder) super.element(entry);
        }

        public JsonObjectBuilder.SubArrayBuilder elements(Iterable<? extends JsonElement> entry) {
            return (JsonObjectBuilder.SubArrayBuilder) super.elements(entry);
        }

        public JsonObjectBuilder.SubArrayBuilder combine(JsonArray array) {
            return (JsonObjectBuilder.SubArrayBuilder) super.combine(array);
        }

        public JsonObjectBuilder.SubArrayBuilder serializable(JsonSerializable serializable) {
            return (JsonObjectBuilder.SubArrayBuilder) super.serializable(serializable);
        }

        public JsonObjectBuilder.SubArrayBuilder serializableObjects(Iterable<? extends JsonSerializable> serializableObjects) {
            return (JsonObjectBuilder.SubArrayBuilder) super.serializableObjects(serializableObjects);
        }

        public JsonObjectBuilder close() {
            JsonObjectBuilder.this.element(key, JsonObjectBuilder.SubArrayBuilder.this.build());
            return JsonObjectBuilder.this;
        }

        public JsonObjectBuilder.SubObjectBuilder closeAsSub() {
            return (JsonObjectBuilder.SubObjectBuilder) close();
        }
    }
}
