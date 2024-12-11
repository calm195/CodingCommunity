package cor.chrissy.community.common.req.user;

import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
public class UserRelationReq {

    /**
     * 用户关系ID
     */
    private Long userRelationId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 关注用户ID
     */
    private Long followUserId;

    /**
     * 关注状态: 0-未关注，1-已关注，2-取消关注
     */
    private Integer followState;
}
