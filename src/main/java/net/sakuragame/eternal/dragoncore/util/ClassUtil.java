package net.sakuragame.eternal.dragoncore.util;

@SuppressWarnings("unchecked")
public class ClassUtil {

    public static <T> T safeCast(Object obj, Class<T> type) {
        if (type.isInstance(obj)) {
            return (T) obj;
        }
        return null;
    }
}