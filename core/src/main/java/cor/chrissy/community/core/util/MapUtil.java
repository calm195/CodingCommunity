package cor.chrissy.community.core.util;

import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 自建Map工具类，使用com.google.common.collect.Maps具体实现
 *
 * @author wx128
 * @createAt 2024/12/9
 */
public class MapUtil {
    public static <K, V> Map<K, V> createHashMap(K key, V value, Object... values) {
        Map<K, V> map = Maps.newHashMapWithExpectedSize(values.length + 1);
        map.put(key, value);

        for (int i = 0; i < values.length; i += 2) {
            map.put((K) values[i], (V) values[i + 1]);
        }
        return map;
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> list, Function<T, K> key, Function<T, V> val) {
        if (CollectionUtils.isEmpty(list)) {
            return Maps.newHashMapWithExpectedSize(0);
        }
        return list.stream().collect(Collectors.toMap(key, val));
    }
}
