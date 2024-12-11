package cor.chrissy.community.service.user.service.impl;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.user.UserRelationReq;
import cor.chrissy.community.service.user.converter.UserConverter;
import cor.chrissy.community.service.user.dto.UserFollowDTO;
import cor.chrissy.community.service.user.dto.UserFollowListDTO;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;
import cor.chrissy.community.service.user.repository.mapper.UserRelationMapper;
import cor.chrissy.community.service.user.service.UserRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Service
public class UserRelationServiceImpl implements UserRelationService {

    @Resource
    private UserRelationMapper userRelationMapper;

    @Resource
    private UserConverter userConverter;

    @Override
    public UserFollowListDTO getUserFollowList(Long userId, PageParam pageParam) {

        UserFollowListDTO userFollowListDTO = new UserFollowListDTO();
        List<UserFollowDTO> userRelationList = userRelationMapper.queryUserFollowList(userId, pageParam);
        if (userRelationList.isEmpty()) {
            return userFollowListDTO;
        }

        Boolean isMore = (userRelationList.size() == pageParam.getPageSize()) ? Boolean.TRUE : Boolean.FALSE;

        userFollowListDTO.setUserFollowList(userRelationList);
        userFollowListDTO.setIsMore(isMore);
        return userFollowListDTO;
    }

    @Override
    public UserFollowListDTO getUserFansList(Long userId, PageParam pageParam) {

        UserFollowListDTO userFollowListDTO = new UserFollowListDTO();
        List<UserFollowDTO> userRelationList = userRelationMapper.queryUserFansList(userId, pageParam);
        if (userRelationList.isEmpty()) {
            return userFollowListDTO;
        }

        Boolean isMore = (userRelationList.size() == pageParam.getPageSize()) ? Boolean.TRUE : Boolean.FALSE;

        userFollowListDTO.setUserFollowList(userRelationList);
        userFollowListDTO.setIsMore(isMore);
        return userFollowListDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRelation(UserRelationReq req) throws Exception {
        if (req.getUserRelationId() == null || req.getUserRelationId() == 0) {
            userRelationMapper.insert(userConverter.toDO(req));
            return;
        }

        UserRelationDO userRelationDO = userRelationMapper.selectById(req.getUserRelationId());
        if (userRelationDO == null) {
            throw new Exception("未查询到该用户关系");
        }
        userRelationMapper.updateById(userConverter.toDO(req));
    }
}

