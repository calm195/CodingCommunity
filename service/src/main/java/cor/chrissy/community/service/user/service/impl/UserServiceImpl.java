package cor.chrissy.community.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cor.chrissy.community.core.enums.YesOrNoEnum;
import cor.chrissy.community.service.user.repository.entity.UserDO;
import cor.chrissy.community.service.user.repository.entity.UserInfoDO;
import cor.chrissy.community.service.user.repository.mapper.UserInfoMapper;
import cor.chrissy.community.service.user.repository.mapper.UserMapper;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wx128
 * @date 2024/12/9
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public void updateUser(UserDO userDTO) {
        UserDO updateUser = userMapper.selectById(userDTO.getId());
        if (updateUser != null) {
            userMapper.updateById(userDTO);
        }
    }

    @Override
    public void deleteUser(Long userInfoId) {
        UserDO updateUser = userMapper.selectById(userInfoId);
        if (updateUser != null) {
            updateUser.setDeleted(YesOrNoEnum.YES.getCode());
            userMapper.updateById(updateUser);
        }
    }

    @Override
    public void updateUserInfo(UserInfoDO userInfoDTO) {
        UserInfoDO updateUserInfo = userInfoMapper.selectById(userInfoDTO.getId());
        if (updateUserInfo != null) {
            userInfoMapper.updateById(userInfoDTO);
        }
    }

    @Override
    public void deleteUserInfo(Long userId) {
        UserInfoDO updateUserInfo = userInfoMapper.selectById(userId);
        if (updateUserInfo != null) {
            updateUserInfo.setDeleted(YesOrNoEnum.YES.getCode());
            userInfoMapper.updateById(updateUserInfo);
        }
    }

    @Override
    public UserInfoDO getUserInfoByUserId(Long userId) {
        LambdaQueryWrapper<UserInfoDO> query = Wrappers.lambdaQuery();
        query.eq(UserInfoDO::getUserId, userId)
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode());
        return userInfoMapper.selectOne(query);
    }
}
