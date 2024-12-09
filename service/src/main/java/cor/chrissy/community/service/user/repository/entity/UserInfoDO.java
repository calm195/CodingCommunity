package cor.chrissy.community.service.user.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import cor.chrissy.community.service.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wx128
 * @date 2024/12/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_info")
public class UserInfoDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户图像
     */
    private String photo;

    /**
     * 职位
     */
    private String position;

    /**
     * 公司
     */
    private String company;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 扩展字段
     */
    private String extend;

    private Integer deleted;
}
