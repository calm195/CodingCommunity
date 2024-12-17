package cor.chrissy.community.service.user.converter;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserRelationReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.service.user.dto.UserHomeDTO;
import cor.chrissy.community.service.user.repository.entity.UserDO;
import cor.chrissy.community.service.user.repository.entity.UserInfoDO;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户信息转换工具
 *
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

    public UserInfoDO toDO(BaseUserInfoDTO baseUserInfoDTO) {
        if (baseUserInfoDTO == null) {
            return null;
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setUserId(baseUserInfoDTO.getUserId());
        userInfoDO.setUserName(baseUserInfoDTO.getUserName());
        userInfoDO.setPhoto(baseUserInfoDTO.getPhoto());
        userInfoDO.setProfile(baseUserInfoDTO.getProfile());
        userInfoDO.setPosition(baseUserInfoDTO.getPosition());
        userInfoDO.setCompany(baseUserInfoDTO.getCompany());
        userInfoDO.setExtend(baseUserInfoDTO.getExtend());
        userInfoDO.setDeleted(baseUserInfoDTO.getDeleted());
        userInfoDO.setId(baseUserInfoDTO.getId());
        userInfoDO.setCreateTime(baseUserInfoDTO.getCreateTime());
        userInfoDO.setUpdateTime(baseUserInfoDTO.getUpdateTime());
        return userInfoDO;
    }

    public UserHomeDTO toUserHomeDTO(BaseUserInfoDTO baseUserInfoDTO) {
        if (baseUserInfoDTO == null) {
            return null;
        }
        UserHomeDTO userHomeDTO = new UserHomeDTO();
        userHomeDTO.setUserId(baseUserInfoDTO.getUserId());
        userHomeDTO.setUserName(baseUserInfoDTO.getUserName());
        userHomeDTO.setRole(baseUserInfoDTO.getRole());
        userHomeDTO.setPhoto(baseUserInfoDTO.getPhoto());
        userHomeDTO.setProfile(baseUserInfoDTO.getProfile());
        userHomeDTO.setPosition(baseUserInfoDTO.getPosition());
        userHomeDTO.setCompany(baseUserInfoDTO.getCompany());
        userHomeDTO.setExtend(baseUserInfoDTO.getExtend());
        userHomeDTO.setDeleted(baseUserInfoDTO.getDeleted());
        userHomeDTO.setId(baseUserInfoDTO.getId());
        userHomeDTO.setCreateTime(baseUserInfoDTO.getCreateTime());
        userHomeDTO.setUpdateTime(baseUserInfoDTO.getUpdateTime());
        return userHomeDTO;
    }

    public BaseUserInfoDTO toDTO(UserInfoDO info) {
        if (info == null) {
            return null;
        }
        BaseUserInfoDTO user = new BaseUserInfoDTO();
        // todo 知识点，bean属性拷贝的几种方式， 直接get/set方式，使用BeanUtil工具类(spring, cglib, apache, objectMapper)，序列化方式等
        BeanUtils.copyProperties(info, user);
        return user;
    }
}
