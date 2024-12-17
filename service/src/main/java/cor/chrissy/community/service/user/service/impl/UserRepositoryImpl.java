package cor.chrissy.community.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cor.chrissy.community.common.enums.FollowStateEnum;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;
import cor.chrissy.community.service.user.repository.mapper.UserRelationMapper;
import cor.chrissy.community.service.user.service.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wx128
 * @createAt 2024/12/15
 */
@Service
public class UserRepositoryImpl implements UserRepository {
    @Resource
    private UserRelationMapper userRelationMapper;

    @Override
    public Long queryUserFollowCount(Long userId) {
        QueryWrapper<UserRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRelationDO::getFollowUserId, userId)
                .eq(UserRelationDO::getFollowStat, FollowStateEnum.FOLLOW.getCode());
        return userRelationMapper.selectCount(queryWrapper);
    }

    @Override
    public Long queryUserFansCount(Long userId) {
        QueryWrapper<UserRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRelationDO::getUserId, userId)
                .eq(UserRelationDO::getFollowStat, FollowStateEnum.FOLLOW.getCode());
        return userRelationMapper.selectCount(queryWrapper);
    }
}
