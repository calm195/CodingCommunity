package cor.chrissy.community.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户信息DTO基类
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseUserInfoDTO extends BaseDTO {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户角色 admin, normal
     */
    private String role;

    /**
     * 用户图像
     */
    private String photo;
    /**
     * 个人简介
     */
    private String profile;
    /**
     * 职位
     */
    private String position;

    /**
     * 公司
     */
    private String company;

    /**
     * 扩展字段
     */
    private String extend;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 用户最后登录区域
     */
    private String region;
}
