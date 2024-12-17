package cor.chrissy.community.service.user.service;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.service.user.dto.UserHomeDTO;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface UserService {

    /**
     * 用户存在时，直接返回；不存在时，则初始化
     *
     * @param req
     */
    void registerOrGetUserInfo(UserSaveReq req);

    /**
     * 保存用户
     *
     * @param req
     */
    void saveUser(UserSaveReq req);

    /**
     * 删除用户
     *
     * @param userId
     */
    void deleteUser(Long userId);

    /**
     * 保存用户详情
     *
     * @param req
     */
    void saveUserInfo(UserInfoSaveReq req);

    /**
     * 删除用户信息
     *
     * @param userId
     */
    void deleteUserInfo(Long userId);

    /**
     * 查询用户详情信息
     *
     * @param userId
     * @return
     */
    BaseUserInfoDTO getUserInfoByUserId(Long userId);


    /**
     * 查询用户主页信息
     *
     * @param userId
     * @return
     */
    UserHomeDTO getUserHomeDTO(Long userId);
}
