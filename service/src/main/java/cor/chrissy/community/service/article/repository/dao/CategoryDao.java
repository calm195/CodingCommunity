package cor.chrissy.community.service.article.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;
import cor.chrissy.community.service.article.repository.mapper.CategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Repository
public class CategoryDao extends ServiceImpl<CategoryMapper, CategoryDO> {
    /**
     * @return
     */
    public List<CategoryDO> listAllCategoriesFromDb() {
        return lambdaQuery()
                .eq(CategoryDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(CategoryDO::getStatus, PushStatEnum.ONLINE.getCode())
                .list();
    }

    /**
     * 获取所有 Categorys 列表（分页）
     *
     * @return
     */
    public List<CategoryDTO> listCategory(PageParam pageParam) {
        List<CategoryDO> list = lambdaQuery()
                .eq(CategoryDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .list();
        return ArticleConverter.toCategoryDtoList(list);
    }

    /**
     * 获取所有 Categorys 总数（分页）
     *
     * @return
     */
    public Integer countCategory() {
        return lambdaQuery()
                .eq(CategoryDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count()
                .intValue();
    }
}
