package cor.chrissy.community.service.user.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.user.UserRelationReq;
import cor.chrissy.community.service.user.dto.UserFollowListDTO;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface UserRelationService {
    /**
     * 我关注的用户
     *
     * @param userId
     * @param pageParam
     * @return
     */
    UserFollowListDTO getUserFollowList(Long userId, PageParam pageParam);


    /**
     * 关注我的粉丝
     *
     * @param userId
     * @param pageParam
     * @return
     */
    UserFollowListDTO getUserFansList(Long userId, PageParam pageParam);


    /**
     * 保存用户关系
     *
     * @param req
     */
    void saveUserRelation(UserRelationReq req);
}
