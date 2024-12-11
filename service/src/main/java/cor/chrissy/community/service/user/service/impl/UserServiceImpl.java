package cor.chrissy.community.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.service.article.dto.ArticleFootCountDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.repository.mapper.ArticleMapper;
import cor.chrissy.community.service.user.converter.UserConverter;
import cor.chrissy.community.service.user.dto.UserHomeDTO;
import cor.chrissy.community.service.user.repository.entity.UserDO;
import cor.chrissy.community.service.user.repository.entity.UserInfoDO;
import cor.chrissy.community.service.user.repository.mapper.UserFootMapper;
import cor.chrissy.community.service.user.repository.mapper.UserInfoMapper;
import cor.chrissy.community.service.user.repository.mapper.UserMapper;
import cor.chrissy.community.service.user.repository.mapper.UserRelationMapper;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserRelationMapper userRelationMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserFootMapper userFootMapper;

    @Resource
    private UserConverter userConverter;

    @Override
    public void saveUser(UserSaveReq req) {
        if (req.getUserId() == null || req.getUserId() == 0) {
            UserDO user = userConverter.toDO(req);
            userMapper.insert(user);
            req.setUserId(user.getId());
            return;
        }

        UserDO userDO = userMapper.selectById(req.getUserId());
        if (userDO == null) {
            throw new IllegalArgumentException("未查询到该用户");
        }
        userMapper.updateById(userConverter.toDO(req));
    }


    @Override
    public void deleteUser(Long userId) {
        UserDO updateUser = userMapper.selectById(userId);
        if (updateUser == null) {
            throw new IllegalArgumentException("未查询到该用户");
        }
        updateUser.setDeleted(YesOrNoEnum.YES.getCode());
        userMapper.updateById(updateUser);
    }

    @Override
    public void saveUserInfo(UserInfoSaveReq req) {
        UserInfoDO userInfoDO = getUserInfoByUserId(req.getUserId());
        if (userInfoDO == null) {
            userInfoMapper.insert(userConverter.toDO(req));
            return;
        }

        UserInfoDO updateUserInfoDO = userConverter.toDO(req);
        updateUserInfoDO.setId(userInfoDO.getId());
        userInfoMapper.updateById(updateUserInfoDO);
    }

    @Override
    public void deleteUserInfo(Long userId) {
        UserInfoDO updateUserInfo = userInfoMapper.selectById(userId);
        if (updateUserInfo != null) {
            updateUserInfo.setDeleted(YesOrNoEnum.YES.getCode());
            userInfoMapper.updateById(updateUserInfo);
        }
    }

    @Override
    public UserInfoDO getUserInfoByUserId(Long userId) {
        LambdaQueryWrapper<UserInfoDO> query = Wrappers.lambdaQuery();
        query.eq(UserInfoDO::getUserId, userId)
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode());
        return userInfoMapper.selectOne(query);
    }

    @Override
    public UserHomeDTO getUserHomeDTO(Long userId) {

        UserInfoDO userInfoDO = getUserInfoByUserId(userId);

        if (userInfoDO == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 获取关注数、粉丝数
        Integer followCount = userRelationMapper.queryUserFollowCount(userId, null);
        Integer fansCount = userRelationMapper.queryUserFansCount(userId, null);

        // 获取文章相关统计
        ArticleFootCountDTO articleFootCountDTO = userFootMapper.queryArticleFootCount(userId);

        // 获取发布文章总数
        LambdaQueryWrapper<ArticleDO> articleQuery = Wrappers.lambdaQuery();
        articleQuery.eq(ArticleDO::getAuthorId, userId)
                .eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode())
                .eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode());
        Long articleCount = articleMapper.selectCount(articleQuery);

        UserHomeDTO userHomeDTO = new UserHomeDTO();
        userHomeDTO.setUserId(userInfoDO.getUserId());
        userHomeDTO.setRole("normal");
        userHomeDTO.setUserName(userInfoDO.getUserName());
        userHomeDTO.setPhoto(userInfoDO.getPhoto());
        userHomeDTO.setProfile(userInfoDO.getProfile());
        userHomeDTO.setFollowCount(followCount);
        userHomeDTO.setFansCount(fansCount);
        if (articleFootCountDTO != null) {
            userHomeDTO.setPraiseCount(articleFootCountDTO.getPraiseCount());
            userHomeDTO.setReadCount(articleFootCountDTO.getReadCount());
            userHomeDTO.setCollectionCount(articleFootCountDTO.getCollectionCount());
        } else {
            userHomeDTO.setPraiseCount(0);
            userHomeDTO.setReadCount(0);
            userHomeDTO.setCollectionCount(0);
        }
        userHomeDTO.setArticleCount(articleCount.intValue());
        return userHomeDTO;
    }
}
