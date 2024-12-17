package cor.chrissy.community.core.permission;

/**
 * 用户权限枚举
 * <p>
 * 管理员 - ADMIN
 * 普通用户 - LOGIN
 * 普通访客 - ALL
 * </p>
 *
 * @author wx128
 * @createAt 2024/12/13
 */
public enum UserRole {
    /**
     * 管理员
     */
    ADMIN,
    /**
     * 登录用户
     */
    LOGIN,
    /**
     * 所有用户，即普通访客
     */
    ALL;
}
