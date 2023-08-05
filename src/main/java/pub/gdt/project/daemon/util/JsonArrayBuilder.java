package pub.gdt.project.daemon.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import pub.gdt.project.daemon.basic.JsonSerializable;

import java.util.function.UnaryOperator;

public class JsonArrayBuilder {
    private final JsonArray result;

    public JsonArrayBuilder() {
        result = new JsonArray();
    }

    public JsonArrayBuilder(JsonArray original) {
        result = original;
    }

    public JsonArrayBuilder number(Number entry) {
        result.add(entry);
        return this;
    }
    
    public JsonArrayBuilder numbers(Iterable<? extends Number> entries) {
        for (Number entry : entries) number(entry);
        return this;
    }

    public JsonArrayBuilder bool(Boolean entry) {
        result.add(entry);
        return this;
    }

    public JsonArrayBuilder character(Character entry) {
        result.add(entry);
        return this;
    }

    public JsonArrayBuilder characters(Iterable<Character> entries) {
        for (Character entry : entries) character(entry);
        return this;
    }

    public JsonArrayBuilder string(String entry) {
        result.add(entry);
        return this;
    }

    public JsonArrayBuilder strings(Iterable<String> entries) {
        for (String entry : entries) string(entry);
        return this;
    }

    public JsonArrayBuilder element(JsonElement entry) {
        result.add(entry);
        return this;
    }

    public JsonArrayBuilder elements(Iterable<? extends JsonElement> entries) {
        for (JsonElement entry : entries) element(entry);
        return this;
    }

    public JsonArrayBuilder.SubObjectBuilder subObject() {
        return new JsonArrayBuilder.SubObjectBuilder();
    }

    public JsonArrayBuilder.SubArrayBuilder subArray() {
        return new JsonArrayBuilder.SubArrayBuilder();
    }

    public JsonArrayBuilder combine(JsonArray array) {
        result.addAll(array);
        return this;
    }

    public JsonArrayBuilder serializable(JsonSerializable serializable) {
        return element(serializable.serialize());
    }

    public JsonArrayBuilder serializableObjects(Iterable<? extends JsonSerializable> serializableObjects) {
        for (JsonSerializable serializableObject : serializableObjects)
            serializable(serializableObject);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends JsonArrayBuilder> T applyOperation(UnaryOperator<T> operator) {
        return operator.apply((T) this);
    }
    
    public JsonArray build() {
        return result;
    }

    public class SubObjectBuilder extends JsonObjectBuilder {
        public JsonArrayBuilder.SubObjectBuilder property(String key, Number value) {
            return (JsonArrayBuilder.SubObjectBuilder) super.property(key, value);
        }

        public JsonArrayBuilder.SubObjectBuilder property(String key, Boolean value) {
            return (JsonArrayBuilder.SubObjectBuilder) super.property(key, value);
        }

        public JsonArrayBuilder.SubObjectBuilder property(String key, Character value) {
            return (JsonArrayBuilder.SubObjectBuilder) super.property(key, value);
        }

        public JsonArrayBuilder.SubObjectBuilder property(String key, String value) {
            return (JsonArrayBuilder.SubObjectBuilder) super.property(key, value);
        }

        public JsonArrayBuilder.SubObjectBuilder element(String key, JsonElement value) {
            return (JsonArrayBuilder.SubObjectBuilder) super.element(key, value);
        }

        public JsonArrayBuilder.SubObjectBuilder jsonNull(String key) {
            return (JsonArrayBuilder.SubObjectBuilder) super.jsonNull(key);
        }

        public JsonArrayBuilder.SubObjectBuilder serializable(String key, JsonSerializable serializableObject) {
            return (JsonArrayBuilder.SubObjectBuilder) super.serializable(key, serializableObject);
        }

        public JsonArrayBuilder close() {
            JsonArrayBuilder.this.element(build());
            return JsonArrayBuilder.this;
        }
    }

    public class SubArrayBuilder extends JsonArrayBuilder {
        public JsonArrayBuilder.SubArrayBuilder number(Number entry) {
            return (JsonArrayBuilder.SubArrayBuilder) super.number(entry);
        }

        public JsonArrayBuilder.SubArrayBuilder numbers(Iterable<? extends Number> entries) {
            return (JsonArrayBuilder.SubArrayBuilder) super.numbers(entries);
        }

        public JsonArrayBuilder.SubArrayBuilder bool(Boolean entry) {
            return (JsonArrayBuilder.SubArrayBuilder) super.bool(entry);
        }

        public JsonArrayBuilder.SubArrayBuilder character(Character entry) {
            return (JsonArrayBuilder.SubArrayBuilder) super.character(entry);
        }

        public JsonArrayBuilder.SubArrayBuilder characters(Iterable<Character> entries) {
            return (JsonArrayBuilder.SubArrayBuilder) super.characters(entries);
        }

        public JsonArrayBuilder.SubArrayBuilder string(String entry) {
            return (JsonArrayBuilder.SubArrayBuilder) super.string(entry);
        }

        public JsonArrayBuilder.SubArrayBuilder strings(Iterable<String> entries) {
            return (JsonArrayBuilder.SubArrayBuilder) super.strings(entries);
        }

        public JsonArrayBuilder.SubArrayBuilder element(JsonElement entry) {
            return (JsonArrayBuilder.SubArrayBuilder) super.element(entry);
        }

        public JsonArrayBuilder.SubArrayBuilder elements(Iterable<? extends JsonElement> entry) {
            return (JsonArrayBuilder.SubArrayBuilder) super.elements(entry);
        }

        public JsonArrayBuilder.SubArrayBuilder combine(JsonArray array) {
            return (JsonArrayBuilder.SubArrayBuilder) super.combine(array);
        }

        public JsonArrayBuilder.SubArrayBuilder serializable(JsonSerializable serializable) {
            return (JsonArrayBuilder.SubArrayBuilder) super.serializable(serializable);
        }

        public JsonArrayBuilder.SubArrayBuilder serializableObjects(Iterable<? extends JsonSerializable> serializableObjects) {
            return (JsonArrayBuilder.SubArrayBuilder) super.serializableObjects(serializableObjects);
        }
        
        public JsonArrayBuilder close() {
            JsonArrayBuilder.this.element(JsonArrayBuilder.SubArrayBuilder.this.build());
            return JsonArrayBuilder.this;
        }

        public JsonArrayBuilder.SubArrayBuilder closeAsSub() {
            return (JsonArrayBuilder.SubArrayBuilder) close();
        }
    }
}
