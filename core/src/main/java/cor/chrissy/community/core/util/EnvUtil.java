package cor.chrissy.community.core.util;

import org.springframework.util.Assert;

/**
 * 项目配置工具类
 *
 * @author wx128
 * @createAt 2024/12/13
 */
public class EnvUtil {
    private static volatile EnvEnum env;

    public enum EnvEnum {
        DEV("dev", false),
        TEST("test", false),
        PRE("pre", false),
        PROD("prod", true);
        private final String env;
        private final boolean prod;

        EnvEnum(String env, boolean prod) {
            this.env = env;
            this.prod = prod;
        }

        public static EnvEnum nameOf(String name) {
            for (EnvEnum env : values()) {
                if (env.env.equalsIgnoreCase(name)) {
                    return env;
                }
            }
            return null;
        }
    }

    public static boolean isPro() {
        return getEnv().prod;
    }

    public static EnvEnum getEnv() {
        if (env == null) {
            synchronized (EnvUtil.class) {
                if (env == null) {
                    env = EnvEnum.nameOf(SpringUtil.getConfig("env.name"));
                }
            }
        }
        Assert.isTrue(env != null, "env.name环境配置必然存在!");
        return env;
    }
}