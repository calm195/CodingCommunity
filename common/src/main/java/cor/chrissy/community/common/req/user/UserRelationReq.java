package cor.chrissy.community.common.req.user;

import lombok.Data;

/**
 * 用户关系请求体
 *
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
public class UserRelationReq {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 粉丝用户ID
     */
    private Long followUserId;

    /**
     * 关注状态
     */
    private Boolean followed;
}
