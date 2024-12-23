package cor.chrissy.community.service.user.service.impl;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.FollowStateEnum;
import cor.chrissy.community.common.enums.NotifyTypeEnum;
import cor.chrissy.community.common.notify.NotifyMsgEvent;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.user.UserRelationReq;
import cor.chrissy.community.core.util.SpringUtil;
import cor.chrissy.community.service.user.converter.UserConverter;
import cor.chrissy.community.service.user.dto.UserFollowDTO;
import cor.chrissy.community.service.user.dto.UserFollowListDTO;
import cor.chrissy.community.service.user.repository.dao.UserRelationDao;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;
import cor.chrissy.community.service.user.service.UserRelationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class UserRelationServiceImpl implements UserRelationService {
    @Resource
    private UserRelationDao userRelationDao;


    @Override
    public UserFollowListDTO getUserFollowList(Long userId, PageParam pageParam) {
        List<UserFollowDTO> userRelationList = userRelationDao.listUserFollows(userId, pageParam);
        return buildRes(userRelationList, pageParam);
    }

    @Override
    public UserFollowListDTO getUserFansList(Long userId, PageParam pageParam) {
        List<UserFollowDTO> userRelationList = userRelationDao.listUserFans(userId, pageParam);
        return buildRes(userRelationList, pageParam);
    }

    private UserFollowListDTO buildRes(List<UserFollowDTO> records, PageParam param) {
        if (CollectionUtils.isEmpty(records)) {
            return UserFollowListDTO.emptyInstance();
        }

        UserFollowListDTO userFollowListDTO = new UserFollowListDTO();
        userFollowListDTO.setUserFollowList(records);
        userFollowListDTO.setIsMore(records.size() == param.getPageSize());
        return userFollowListDTO;
    }

    @Override
    public void saveUserRelation(UserRelationReq req) {
        // 查询是否存在
        UserRelationDO userRelationDO = userRelationDao.getUserRelationRecord(req.getUserId(), ReqInfoContext.getReqInfo().getUserId());
        if (userRelationDO == null) {
            userRelationDao.save(UserConverter.toDO(req));
            // 发布关注事件
            SpringUtil.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.FOLLOW, userRelationDO));
            return;
        }
        userRelationDO.setFollowStat(req.getFollowed() ? FollowStateEnum.FOLLOW.getCode() : FollowStateEnum.CANCEL_FOLLOW.getCode());
        userRelationDao.updateById(userRelationDO);
        // 发布关注事件
        SpringUtil.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.FOLLOW, userRelationDO));
    }
}

