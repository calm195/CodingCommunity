package cor.chrissy.community.service.user.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cor.chrissy.community.common.enums.FollowStateEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.user.dto.UserFollowDTO;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;
import cor.chrissy.community.service.user.repository.mapper.UserRelationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Repository
public class UserRelationDao extends ServiceImpl<UserRelationMapper, UserRelationDO> {

    /**
     * 查询用户的关注列表
     *
     * @param followUserId
     * @param pageParam
     * @return
     */
    public List<UserFollowDTO> listUserFollows(Long followUserId, PageParam pageParam) {
        return baseMapper.queryUserFollowList(followUserId, pageParam);
    }

    /**
     * 查询用户的粉丝列表，即关注userId的用户
     *
     * @param userId
     * @param pageParam
     * @return
     */
    public List<UserFollowDTO> listUserFans(Long userId, PageParam pageParam) {
        return baseMapper.queryUserFansList(userId, pageParam);
    }

    public Long queryUserFollowCount(Long userId) {
        QueryWrapper<UserRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRelationDO::getFollowUserId, userId)
                .eq(UserRelationDO::getFollowStat, FollowStateEnum.FOLLOW.getCode());
        return baseMapper.selectCount(queryWrapper);
    }

    public Long queryUserFansCount(Long userId) {
        QueryWrapper<UserRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRelationDO::getUserId, userId)
                .eq(UserRelationDO::getFollowStat, FollowStateEnum.FOLLOW.getCode());
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 获取关注信息
     *
     * @param userId  登录用户
     * @param followUserId  关注的用户
     * @return
     */
    public UserRelationDO getUserRelationByUserId(Long userId, Long followUserId) {
        QueryWrapper<UserRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRelationDO::getUserId, userId)
                .eq(UserRelationDO::getFollowUserId, followUserId)
                .eq(UserRelationDO::getFollowStat, FollowStateEnum.FOLLOW.getCode());
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 获取关注记录
     *
     * @param userId  登录用户
     * @param followUserId  关注的用户
     * @return
     */
    public UserRelationDO getUserRelationRecord(Long userId, Long followUserId) {
        QueryWrapper<UserRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRelationDO::getUserId, userId)
                .eq(UserRelationDO::getFollowUserId, followUserId);
        return baseMapper.selectOne(queryWrapper);
    }
}