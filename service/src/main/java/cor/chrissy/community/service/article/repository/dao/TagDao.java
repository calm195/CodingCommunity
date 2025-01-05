package cor.chrissy.community.service.article.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.repository.entity.TagDO;
import cor.chrissy.community.service.article.repository.mapper.TagMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Repository
public class TagDao extends ServiceImpl<TagMapper, TagDO> {

    public List<TagDTO> listOnlineTag(String key, PageParam param) {
        LambdaQueryWrapper<TagDO> query = Wrappers.lambdaQuery();
        query.eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(TagDO::getStatus, PushStatEnum.ONLINE.getCode())
                .and(!StringUtils.isEmpty(key), v -> v.like(TagDO::getTagName, key))
                .orderByDesc(TagDO::getId);
        if (param != null) {
            query.last(PageParam.getLimitSql(param));
        }
        List<TagDO> list = baseMapper.selectList(query);
        return ArticleConverter.toDtoList(list);
    }

    public Integer countOnlineTag(String key) {
        return lambdaQuery()
                .eq(TagDO::getStatus, PushStatEnum.ONLINE.getCode())
                .eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .and(!StringUtils.isEmpty(key), v -> v.like(TagDO::getTagName, key))
                .count()
                .intValue();
    }

    /**
     * 获取所有 Tags 列表（分页）
     *
     * @return
     */
    public List<TagDTO> listTag(PageParam pageParam) {
        List<TagDO> list = lambdaQuery()
                .eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(TagDO::getId)
                .last(PageParam.getLimitSql(pageParam))
                .list();
        return ArticleConverter.toDtoList(list);
    }

    /**
     * 获取所有 Tags 总数（分页）
     *
     * @return
     */
    public Integer countTag() {
        return lambdaQuery()
                .eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count()
                .intValue();
    }

    public Long selectTagIdByTag(String tag) {
        TagDO record = lambdaQuery().select(TagDO::getId)
                .eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(TagDO::getTagName, tag)
                .last("limit 1")
                .one();
        return record == null ? 0L : record.getId();
    }
}

