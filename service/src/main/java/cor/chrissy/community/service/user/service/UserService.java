package cor.chrissy.community.service.user.service;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.service.user.dto.UserStatisticInfoDTO;

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
     * 保存用户详情
     *
     * @param req
     */
    void saveUserInfo(UserInfoSaveReq req);

    /**
     * 查询用户基本信息
     * todo: 可以做缓存优化
     *
     * @param userId
     * @return
     */
    BaseUserInfoDTO queryBasicUserInfo(Long userId);


    /**
     * 查询用户主页信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    UserStatisticInfoDTO queryUserInfoWithStatistic(Long userId);

    BaseUserInfoDTO passwordLogin(String username, String password);
}
