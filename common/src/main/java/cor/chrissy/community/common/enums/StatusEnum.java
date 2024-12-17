package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 网络请求参数枚举
 * TODO：名字统一 -> stat
 *
 * @author wx128
 * @createAt 2024/12/11
 */
@Getter
public enum StatusEnum {
    SUCCESS(0, "OK"),

    ILLEGAL_ARGUMENTS(400_001, "参数异常"),
    ILLEGAL_ARGUMENTS_MIXED(400_002, "参数异常:%s"),

    ILLEGAL_S(400_002, "参数异常:%s"),

    LOGIN_FAILED_MIXED(403_001, "登录失败:%s"),
    ;

    private int code;

    private String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
