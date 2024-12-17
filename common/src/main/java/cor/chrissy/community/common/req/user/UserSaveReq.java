package cor.chrissy.community.common.req.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户账户保存请求体
 *
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
@Accessors(chain = true)
public class UserSaveReq {
    /**
     * 主键ID
     */
    private Long userId;

    /**
     * 第三方用户ID
     */
    private String thirdAccountId;

    /**
     * 登录方式: 0-微信登录，1-账号密码登录
     */
    private Integer loginType;
}
