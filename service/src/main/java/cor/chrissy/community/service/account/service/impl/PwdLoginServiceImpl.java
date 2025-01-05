package cor.chrissy.community.service.account.service.impl;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.core.util.ExceptionUtil;
import cor.chrissy.community.core.util.IpUtil;
import cor.chrissy.community.service.account.service.LoginService;
import cor.chrissy.community.service.account.service.help.SessionHelper;
import cor.chrissy.community.service.user.converter.UserConverter;
import cor.chrissy.community.service.user.repository.dao.UserDao;
import cor.chrissy.community.service.user.repository.entity.IpInfo;
import cor.chrissy.community.service.user.repository.entity.UserInfoDO;
import cor.chrissy.community.service.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author wx128
 * @createAt 2025/1/4
 */
@Service
public class PwdLoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionHelper sessionHelper;

    @Override
    public String getVerifyCode(String uuid) {
        UserSaveReq req = new UserSaveReq().setLoginType(0).setThirdAccountId(uuid);
        userService.registerOrGetUserInfo(req);
        return sessionHelper.genVerifyCode(req.getUserId());
    }

    @Override
    public String login(String code) {
        Long userId = sessionHelper.getUserIdByCode(code);
        if (userId == null) {
            return null;
        }
        return sessionHelper.codeVerifySucceed(code, userId);
    }

    @Override
    public String login(Long userId) {
        return sessionHelper.codeVerifySucceed("", userId);
    }

    @Override
    public void logout(String session) {
        sessionHelper.removeSession(session);
    }


    @Override
    public BaseUserInfoDTO getAndUpdateUserIpInfoBySessionId(String session, String clientIp) {
        if (StringUtils.isBlank(session)) {
            return null;
        }

        Long userId = sessionHelper.getUserIdBySession(session);
        if (userId == null) {
            return null;
        }

        // 查询用户信息，并更新最后一次使用的ip
        UserInfoDO user = userDao.getByUserId(userId);
        if (user == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, "userId=" + userId);
        }

        IpInfo ip = user.getIp();
        if (!Objects.equals(ip.getLatestIp(), clientIp)) {
            // ip不同，需要更新
            ip.setLatestIp(clientIp);
            ip.setLatestRegion(IpUtil.getLocationByIp(clientIp).toRegionStr());

            if (ip.getFirstIp() == null) {
                ip.setFirstIp(clientIp);
                ip.setFirstRegion(ip.getLatestRegion());
            }
            userDao.updateById(user);
        }
        return UserConverter.toDTO(user);
    }
}
