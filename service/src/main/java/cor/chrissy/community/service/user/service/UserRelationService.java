package cor.chrissy.community.service.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface UserRelationService {
    /**
     * 获取关注用户列表
     * @param userId
     * @return
     */
    IPage<UserRelationDO> getUserRelationListByUserId(Integer userId, PageParam pageParam);

    /**
     * 获取被关注用户列表
     * @param followUserId
     * @return
     */
    IPage<UserRelationDO> getUserRelationListByFollowUserId(Integer followUserId, PageParam pageParam);

    /**
     * 删除用户关系
     * @param id
     */
    void deleteUserRelationById(Long id);
}
