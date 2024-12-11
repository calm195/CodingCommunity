package cor.chrissy.community.service.user.service;

import cor.chrissy.community.service.user.repository.entity.UserDO;
import cor.chrissy.community.service.user.repository.entity.UserInfoDO;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface UserService {
    /**
     * 更新用户
     * @param userDTO
     */
    void updateUser(UserDO userDTO);

    /**
     * 删除用户
     * @param userInfoId
     */
    void deleteUser(Long userInfoId);

    /**
     * 更新用户信息
     * @param userInfoDTO
     */
    void updateUserInfo(UserInfoDO userInfoDTO);

    /**
     * 删除用户信息
     * @param userId
     */
    void deleteUserInfo(Long userId);

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    UserInfoDO getUserInfoByUserId(Long userId);
}
