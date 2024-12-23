package cor.chrissy.community.service.article.repository.dao;

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

    public List<TagDTO> listTagsByCategoryId(Long categoryId) {
        List<TagDO> list = lambdaQuery()
                .eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(TagDO::getCategoryId, categoryId)
                .eq(TagDO::getStatus, PushStatEnum.ONLINE.getCode())
                .list();
        return ArticleConverter.toDtoList(list);
    }

    /**
     * 获取所有 Tags 列表（分页）
     *
     * @return
     */
    public List<TagDTO> listTag(PageParam pageParam) {
        List<TagDO> list = lambdaQuery()
                .eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
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
}

