package cor.chrissy.community.service.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cor.chrissy.community.core.enums.PushStatusEnum;
import cor.chrissy.community.core.req.PageParam;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;

/**
 * @author wx128
 * @date 2024/12/9
 */
public interface CategoryService {
    /**
     * 添加类目
     *
     * @param categoryDO
     * @return
     */
    Long addCategory(CategoryDO categoryDO);

    void updateCategory(Long categoryId, String categoryName);

    /**
     * 删除类目
     *
     * @param categoryId
     */
    void deleteCategory(Long categoryId);

    /**
     * 操作类目
     *
     * @param categoryId
     */
    void operateCategory(Long categoryId, PushStatusEnum pushStatusEnum);

    /**
     * 类目分页查询
     *
     * @return
     */
    IPage<CategoryDO> getCategoryByPage(PageParam pageParam);
}
