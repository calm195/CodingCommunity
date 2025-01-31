package cor.chrissy.community.service.user.repository.dao;

import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.service.user.repository.entity.UserDO;
import cor.chrissy.community.service.user.repository.entity.UserInfoDO;
import cor.chrissy.community.service.user.repository.mapper.UserInfoMapper;
import cor.chrissy.community.service.user.repository.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Repository
public class UserDao extends ServiceImpl<UserInfoMapper, UserInfoDO> {
    @Resource
    private UserMapper userMapper;

    public UserDO getByThirdAccountId(String accountId) {
        return userMapper.getByThirdAccountId(accountId);
    }

    public void saveUser(UserDO user) {
        userMapper.insert(user);
    }

    public UserInfoDO getByUserId(Long userId) {
        LambdaQueryWrapper<UserInfoDO> query = Wrappers.lambdaQuery();
        query.eq(UserInfoDO::getUserId, userId)
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode());
        return baseMapper.selectOne(query);
    }

    public UserDO getByUserName(String userName) {
        LambdaQueryWrapper<UserDO> query = Wrappers.lambdaQuery();
        query.eq(UserDO::getUserName, userName)
                .eq(UserDO::getDeleted, YesOrNoEnum.NO.getCode());
        return userMapper.selectOne(query);
    }

    public Integer getUserCount() {
        return lambdaQuery()
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count().intValue();
    }

    public void updateUserInfo(UserInfoDO user) {
        UserInfoDO record = getByUserId(user.getUserId());
        if (record.equals(user)) {
            return;
        }
        if (StringUtils.isEmpty(user.getPhoto())) {
            user.setPhoto(null);
        }
        if (StringUtils.isEmpty(user.getUserName())) {
            user.setUserName(null);
        }
        user.setId(record.getId());
        updateById(user);
    }

    public List<UserInfoDO> getByUserNameLike(String userName) {
        LambdaQueryWrapper<UserInfoDO> query = Wrappers.lambdaQuery();
        query.select(UserInfoDO::getUserId, UserInfoDO::getUserName, UserInfoDO::getPhoto, UserInfoDO::getProfile)
                .like(UserInfoDO::getUserName, userName)
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode());
        return baseMapper.selectList(query);
    }

    public List<UserInfoDO> getByUserIds(Collection<Long> userIds) {
        LambdaQueryWrapper<UserInfoDO> query = Wrappers.lambdaQuery();
        query.in(UserInfoDO::getUserId, userIds)
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode());
        return baseMapper.selectList(query);
    }
}

