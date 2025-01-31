package cor.chrissy.community.common.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * @author wx128
 * @createAt 2025/1/11
 */
@Getter
public enum RoleEnum {
    NORMAL(0, "普通用户"),
    ADMIN(1, "超级用户"),
    ;

    private final int role;
    private final String desc;

    RoleEnum(int role, String desc) {
        this.role = role;
        this.desc = desc;
    }

    public static String role(Integer roleId) {
        if (Objects.equals(roleId, 1)) {
            return ADMIN.name();
        } else {
            return NORMAL.name();
        }
    }
}
