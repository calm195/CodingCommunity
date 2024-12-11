package cor.chrissy.community.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 自建Map工具类
 *
 * @author wx128
 * @createAt 2024/12/9
 */
public class MapUtil {
    public static <K, V> Map<K, V> createHashMap(K key, V value, Object... values) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);

        for (int i = 0; i < values.length; i += 2) {
            map.put((K) values[i], (V) values[i + 1]);
        }
        return map;
    }
}
