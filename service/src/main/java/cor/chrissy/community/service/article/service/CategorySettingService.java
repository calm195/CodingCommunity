package cor.chrissy.community.service.article.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.CategoryReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.service.article.dto.CategoryDTO;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface CategorySettingService {
    void saveCategory(CategoryReq categoryReq);

    void deleteCategory(Integer categoryId);

    void operateCategory(Integer categoryId, Integer operateType);

    /**
     * 获取category列表
     *
     * @param pageParam
     * @return
     */
    PageVo<CategoryDTO> getCategoryList(PageParam pageParam);
}
