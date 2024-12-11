package cor.chrissy.community.service.user.converter;

import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserRelationReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.service.user.repository.entity.UserDO;
import cor.chrissy.community.service.user.repository.entity.UserInfoDO;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;
import org.springframework.stereotype.Service;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Service
public class UserConverter {

    public UserDO toDO(UserSaveReq req) {
        if (req == null) {
            return null;
        }
        UserDO userDO = new UserDO();
        userDO.setId(req.getUserId());
        userDO.setThirdAccountId(req.getThirdAccountId());
        userDO.setLoginType(req.getLoginType());
        return userDO;
    }

    public UserInfoDO toDO(UserInfoSaveReq req) {
        if (req == null) {
            return null;
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setUserId(req.getUserId());
        userInfoDO.setUserName(req.getUserName());
        userInfoDO.setPhoto(req.getPhoto());
        userInfoDO.setPosition(req.getPosition());
        userInfoDO.setCompany(req.getCompany());
        userInfoDO.setProfile(req.getProfile());
        return userInfoDO;
    }

    public UserRelationDO toDO(UserRelationReq req) {
        if (req == null) {
            return null;
        }
        UserRelationDO userRelationDO = new UserRelationDO();
        userRelationDO.setId(req.getUserRelationId());
        userRelationDO.setUserId(req.getUserId());
        userRelationDO.setFollowUserId(req.getFollowUserId());
        userRelationDO.setFollowStat(req.getFollowState());
        return userRelationDO;
    }
}
