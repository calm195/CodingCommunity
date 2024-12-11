package cor.chrissy.community.service.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface CategoryService {
    /**
     * 查询类目名
     *
     * @param categoryId
     * @return
     */
    String getCategoryName(Long categoryId);

    /**
     * 查询所有的分离
     *
     * @param forceDB
     * @return
     */
    List<CategoryDTO> loadAllCategories(boolean forceDB);

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
    void operateCategory(Long categoryId, PushStatEnum pushStatEnum);

    /**
     * 类目分页查询
     *
     * @return
     */
    IPage<CategoryDO> getCategoryByPage(PageParam pageParam);
}
