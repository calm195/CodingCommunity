package cor.chrissy.community.core.permission;

import java.lang.annotation.*;

/**
 * 自建注解，用于权限认证。
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Permission {
    /**
     * 权限角色。默认普通访客
     *
     * @see cor.chrissy.community.core.permission.UserRole
     * @return
     */
    UserRole role() default UserRole.ALL;
}
