package cor.chrissy.community.service.article.service;

import cor.chrissy.community.service.article.dto.CategoryDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface CategoryService {
    /**
     * 查询类目名
     *
     * @param categoryId
     * @return
     */
    String queryCategoryName(Long categoryId);


    /**
     * 查询所有的分离
     *
     * @return
     */
    List<CategoryDTO> loadAllCategories();

    Long queryCategoryId(String category);
}
