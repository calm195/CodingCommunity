package cor.chrissy.community.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;
import cor.chrissy.community.service.user.repository.mapper.UserRelationMapper;
import cor.chrissy.community.service.user.service.UserRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Service
public class UserRelationServiceImpl implements UserRelationService {

    @Resource
    private UserRelationMapper userRelationMapper;

    @Override
    public IPage<UserRelationDO> getUserRelationListByUserId(Integer userId, PageParam pageParam) {
        LambdaQueryWrapper<UserRelationDO> query = Wrappers.lambdaQuery();
        query.eq(UserRelationDO::getUserId, userId);
        Page page = new Page(pageParam.getPageNum(), pageParam.getPageSize());
        return userRelationMapper.selectPage(page, query);
    }

    @Override
    public IPage<UserRelationDO> getUserRelationListByFollowUserId(Integer followUserId, PageParam pageParam) {
        LambdaQueryWrapper<UserRelationDO> query = Wrappers.lambdaQuery();
        query.eq(UserRelationDO::getFollowUserId, followUserId);
        Page page = new Page(pageParam.getPageNum(), pageParam.getPageSize());
        return userRelationMapper.selectPage(page, query);
    }

    @Override
    public void deleteUserRelationById(Long id) {
        UserRelationDO userRelationDTO = userRelationMapper.selectById(id);
        if (userRelationDTO != null) {
            userRelationMapper.deleteById(id);
        }
    }
}

