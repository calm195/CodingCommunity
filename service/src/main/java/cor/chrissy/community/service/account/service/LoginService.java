package cor.chrissy.community.service.account.service;

import com.google.common.collect.Sets;
import cor.chrissy.community.common.entity.BaseUserInfoDTO;

import java.util.Set;

/**
 * @author wx128
 * @createAt 2024/12/15
 */
public interface LoginService {
    String SESSION_KEY = "f-session";
    Set<String> LOGIN_CODE_KEY = Sets.newHashSet("登录", "login");


    /**
     * 获取登录验证码
     *
     * @param uuid
     * @return
     */
    String getVerifyCode(String uuid);

    /**
     * 登录
     *
     * @param code
     * @return
     */
    String login(String code);

    /**
     * 登出
     *
     * @param session
     */
    void logout(String session);


    /**
     * 获取登录的用户信息
     *
     * @param session
     * @return
     */
    BaseUserInfoDTO getUserBySessionId(String session);
}
