package cor.chrissy.community.service.user.service.impl;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.core.util.ExceptionUtil;
import cor.chrissy.community.service.article.dto.ArticleFootCountDTO;
import cor.chrissy.community.service.article.dto.YearArticleDTO;
import cor.chrissy.community.service.article.repository.dao.ArticleDao;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.article.service.CountService;
import cor.chrissy.community.service.user.converter.UserConverter;
import cor.chrissy.community.service.user.dto.UserStatisticInfoDTO;
import cor.chrissy.community.service.user.repository.dao.UserDao;
import cor.chrissy.community.service.user.repository.dao.UserRelationDao;
import cor.chrissy.community.service.user.repository.entity.UserDO;
import cor.chrissy.community.service.user.repository.entity.UserInfoDO;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;
import cor.chrissy.community.service.user.service.UserService;
import cor.chrissy.community.service.user.service.help.UserRandomGenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private UserRelationDao userRelationDao;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private CountService countService;

    @Autowired
    private ArticleDao articleDao;


    /**
     * 用户存在时，直接返回；不存在时，则初始化
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerOrGetUserInfo(UserSaveReq req) {
        UserDO record = userDao.getByThirdAccountId(req.getThirdAccountId());
        if (record != null) {
            // 用户存在，不需要注册
            req.setUserId(record.getId());
            return;
        }

        // 用户不存在，则需要注册
        record = UserConverter.toDO(req);
        userDao.saveUser(record);
        req.setUserId(record.getId());

        // 初始化用户信息，随机生成用户昵称 + 头像
        UserInfoDO userInfo = new UserInfoDO();
        userInfo.setUserId(req.getUserId());
        userInfo.setUserName(UserRandomGenHelper.genNickName());
        userInfo.setPhoto(UserRandomGenHelper.genAvatar());
        userDao.save(userInfo);
    }

    @Override
    public void saveUserInfo(UserInfoSaveReq req) {
        UserInfoDO userInfoDO = UserConverter.toDO(req);
        userDao.updateById(userInfoDO);
    }

    @Override
    public BaseUserInfoDTO queryBasicUserInfo(Long userId) {
        UserInfoDO user = userDao.getByUserId(userId);
        if (user == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, "userId=" + userId);
        }
        return UserConverter.toDTO(user);
    }

    @Override
    public UserStatisticInfoDTO queryUserInfoWithStatistic(Long userId) {
        BaseUserInfoDTO userInfoDTO = queryBasicUserInfo(userId);
        UserStatisticInfoDTO userHomeDTO = UserConverter.toUserHomeDTO(userInfoDTO);
        userHomeDTO.setRole("normal");

        // 获取文章相关统计
        ArticleFootCountDTO articleFootCountDTO = countService.queryArticleCountInfoByUserId(userId);
        if (articleFootCountDTO != null) {
            userHomeDTO.setPraiseCount(articleFootCountDTO.getPraiseCount());
            userHomeDTO.setReadCount(articleFootCountDTO.getReadCount());
            userHomeDTO.setCollectionCount(articleFootCountDTO.getCollectionCount());
        } else {
            userHomeDTO.setPraiseCount(0);
            userHomeDTO.setReadCount(0);
            userHomeDTO.setCollectionCount(0);
        }

        // 获取发布文章总数
        int articleCount = articleReadService.queryArticleCount(userId);
        userHomeDTO.setArticleCount(articleCount);

        // 获取关注数
        Long followCount = userRelationDao.queryUserFollowCount(userId);
        userHomeDTO.setFollowCount(followCount.intValue());

        // 粉丝数
        Long fansCount = userRelationDao.queryUserFansCount(userId);
        userHomeDTO.setFansCount(fansCount.intValue());

        // 是否关注
        Long followUserId = ReqInfoContext.getReqInfo().getUserId();
        if (followUserId != null) {
            UserRelationDO userRelationDO = userRelationDao.getUserRelationByUserId(userId, followUserId);
            userHomeDTO.setFollowed((userRelationDO == null) ? Boolean.FALSE : Boolean.TRUE);
        } else {
            userHomeDTO.setFollowed(Boolean.FALSE);
        }

        // 加入天数
        Integer joinDayCount = (int) ((new Date()).getTime() - userHomeDTO.getCreateTime().getTime()) / (1000 * 3600 * 24);
        userHomeDTO.setJoinDayCount(joinDayCount);

        // 创作历程
        List<YearArticleDTO> yearArticleDTOS = articleDao.listYearArticleByUserId(userId);
        userHomeDTO.setYearArticleList(yearArticleDTOS);
        return userHomeDTO;
    }
}
