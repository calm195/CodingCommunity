package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * TODO：名字统一 -> stat
 * <p>
 * 异常码规范：
 * xxx - xxx - xxx
 * 业务 - 状态 - code
 * <p>
 * 业务取值
 * - 100 全局
 * - 200 文章相关
 * - 300 评论相关
 * - 400 用户相关
 * <p>
 * 状态：基于http status的含义
 * - 4xx 调用方问题
 * - 5xx 服务内部问题
 * <p>
 * code: 具体的业务code
 *
 * @author wx128
 * @createAt 2024/12/11
 */
@Getter
public enum StatusEnum {
    SUCCESS(0, "OK"),

    // 全局传参异常
    ILLEGAL_ARGUMENTS(100_400_001, "参数异常"),
    ILLEGAL_ARGUMENTS_MIXED(100_400_002, "参数异常:%s"),

    // 全局权限相关
    FORBID_ERROR(100_403_001, "无权限"),

    FORBID_ERROR_MIXED(100_403_002, "无权限:%s"),

    // 全局，数据不存在
    RECORDS_NOT_EXISTS(100_404_001, "记录不存在:%s"),

    // 系统异常
    UNEXPECT_ERROR(100_500_001, "非预期异常:%s"),

    // 图片相关异常类型
    UPLOAD_PIC_FAILED(100_500_002, "图片上传失败！"),

    // 文章相关异常类型，前缀为200
    ARTICLE_NOT_EXISTS(200_404_001, "文章不存在:%s"),
    COLUMN_NOT_EXISTS(200_404_002, "专栏不存在:%s"),
    COLUMN_QUERY_ERROR(200_500_003, "专栏查询异常:%s"),

    // 评论相关异常类型
    COMMENT_NOT_EXISTS(300_404_001, "评论不存在:%s"),

    // 用户相关异常
    LOGIN_FAILED_MIXED(400_403_001, "登录失败:%s"),
    USER_NOT_EXISTS(400_404_001, "用户不存在:%s"),
    USER_PWD_ERROR(400_500_002, "用户名or密码错误"),
    ;

    private final int code;

    private final String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static boolean is5xx(int code) {
        return code % 1000_000 / 1000 >= 500;
    }

    public static boolean is403(int code) {
        return code % 1000_000 / 1000 == 403;
    }

    public static boolean is4xx(int code) {
        return code % 1000_000 / 1000 < 500;
    }
}
