package pub.gdt.project.daemon.basic;

import com.google.gson.JsonElement;

/**
 * Serialize(序列化)的目的是：将数据通过 WebAPI 传递给用户。
 */
public interface JsonSerializable {
    JsonElement serialize();
}
