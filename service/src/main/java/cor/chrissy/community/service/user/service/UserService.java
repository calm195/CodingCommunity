package cor.chrissy.community.service.user.service;

import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.service.user.dto.UserHomeDTO;
import cor.chrissy.community.service.user.repository.entity.UserInfoDO;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface UserService {
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
    UserInfoDO getUserInfoByUserId(Long userId);


    /**
     * 查询用户主页信息
     *
     * @param userId
     * @return
     */
    UserHomeDTO getUserHomeDTO(Long userId);
}
