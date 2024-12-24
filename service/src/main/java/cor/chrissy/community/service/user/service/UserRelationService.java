package cor.chrissy.community.service.user.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.user.UserRelationReq;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.user.dto.FollowUserInfoDTO;

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
    PageListVo<FollowUserInfoDTO> getUserFollowList(Long userId, PageParam pageParam);


    /**
     * 关注我的粉丝
     *
     * @param userId
     * @param pageParam
     * @return
     */
    PageListVo<FollowUserInfoDTO> getUserFansList(Long userId, PageParam pageParam);

    /**
     * 更新当前登录用于与列表中的用户的关注关系
     *
     * @param followList
     * @param loginUserId
     */
    void updateUserFollowRelationId(PageListVo<FollowUserInfoDTO> followList, Long loginUserId);

    /**
     * 保存用户关系
     *
     * @param req
     */
    void saveUserRelation(UserRelationReq req);
}
