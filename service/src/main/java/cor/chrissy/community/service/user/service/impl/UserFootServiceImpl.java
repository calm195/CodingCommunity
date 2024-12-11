package cor.chrissy.community.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cor.chrissy.community.common.enums.CollectionStatEnum;
import cor.chrissy.community.common.enums.CommentStatEnum;
import cor.chrissy.community.common.enums.PraiseStatEnum;
import cor.chrissy.community.common.enums.ReadStatEnum;
import cor.chrissy.community.service.user.repository.entity.UserFootDO;
import cor.chrissy.community.service.user.repository.mapper.UserFootMapper;
import cor.chrissy.community.service.user.service.UserFootService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Service
public class UserFootServiceImpl implements UserFootService {

    @Resource
    private UserFootMapper userFootMapper;

    @Override
    public Long queryCollectionCount(Long documentId) {
        LambdaQueryWrapper<UserFootDO> query = Wrappers.lambdaQuery();
        query.eq(UserFootDO::getDocumentId, documentId)
                .eq(UserFootDO::getCollectionStat, CollectionStatEnum.COLLECTION.getCode());
        return userFootMapper.selectCount(query);
    }

    @Override
    public Long queryReadCount(Long documentId) {
        LambdaQueryWrapper<UserFootDO> query = Wrappers.lambdaQuery();
        query.eq(UserFootDO::getDocumentId, documentId)
                .eq(UserFootDO::getReadStat, ReadStatEnum.READ.getCode());
        return userFootMapper.selectCount(query);
    }

    @Override
    public Long queryCommentCount(Long documentId) {
        LambdaQueryWrapper<UserFootDO> query = Wrappers.lambdaQuery();
        query.eq(UserFootDO::getDocumentId, documentId)
                .eq(UserFootDO::getCommentStat, CommentStatEnum.COMMENT.getCode());
        return userFootMapper.selectCount(query);
    }

    @Override
    public Long queryPraiseCount(Long documentId) {
        LambdaQueryWrapper<UserFootDO> query = Wrappers.lambdaQuery();
        query.eq(UserFootDO::getDocumentId, documentId)
                .eq(UserFootDO::getPraiseStat, PraiseStatEnum.PRAISE.getCode());
        return userFootMapper.selectCount(query);
    }

    @Override
    public Integer operateCollectionFoot(Long documentId, Long userId, CollectionStatEnum statEnum) {
        LambdaQueryWrapper<UserFootDO> query = Wrappers.lambdaQuery();
        query.eq(UserFootDO::getDocumentId, documentId)
                .eq(UserFootDO::getUserId, userId);
        UserFootDO userFootDTO = userFootMapper.selectOne(query);
        userFootDTO.setCollectionStat(statEnum.getCode());
        return userFootMapper.update(userFootDTO, query);
    }

    @Override
    public Integer operateCommentFoot(Long documentId, Long userId, CommentStatEnum statEnum) {
        LambdaQueryWrapper<UserFootDO> query = Wrappers.lambdaQuery();
        query.eq(UserFootDO::getDocumentId, documentId)
                .eq(UserFootDO::getUserId, userId);
        UserFootDO userFootDTO = userFootMapper.selectOne(query);
        userFootDTO.setCommentStat(statEnum.getCode());
        return userFootMapper.update(userFootDTO, query);
    }

    @Override
    public Integer operatePraiseFoot(Long documentId, Long userId, PraiseStatEnum statEnum) {
        LambdaQueryWrapper<UserFootDO> query = Wrappers.lambdaQuery();
        query.eq(UserFootDO::getDocumentId, documentId)
                .eq(UserFootDO::getUserId, userId);
        UserFootDO userFootDTO = userFootMapper.selectOne(query);
        userFootDTO.setPraiseStat(statEnum.getCode());
        return userFootMapper.update(userFootDTO, query);
    }
}
