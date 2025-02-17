package cor.chrissy.community.test.guavacache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author wx128
 * @createAt 2025/1/15
 */
public class CacheBuilderExample {
    public static void main(String[] args) throws ExecutionException {
        // 创建一个 CacheBuilder 对象
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(100)  // 最大缓存条目数
                .expireAfterAccess(30, TimeUnit.MINUTES) // 缓存项在指定时间内没有被访问就过期
                .recordStats();  // 开启统计功能

        // 构建一个 LoadingCache 对象
        LoadingCache<String, String> cache = cacheBuilder.build(new CacheLoader<String, String>() {
            @Override
            public @NotNull String load(@NotNull String key) {
                return "value：" + key; // 当缓存中没有值时，加载对应的值并返回
            }
        });

        // 存入缓存
        cache.put("itwanger", "沉默王二");

        // 从缓存中获取值
        // put 过
        System.out.println(cache.get("itwanger"));
        // 没 put 过
        System.out.println(cache.get("chenqingyang"));

        // 打印缓存的命中率等统计信息
        System.out.println(cache.stats());
    }
}
