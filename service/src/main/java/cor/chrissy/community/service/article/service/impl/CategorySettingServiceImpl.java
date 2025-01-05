package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.CategoryReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.repository.dao.CategoryDao;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;
import cor.chrissy.community.service.article.service.CategoryService;
import cor.chrissy.community.service.article.service.CategorySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class CategorySettingServiceImpl implements CategorySettingService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void saveCategory(CategoryReq categoryReq) {
        CategoryDO categoryDO = ArticleConverter.toDO(categoryReq);
        if (NumUtil.nullOrZero(categoryReq.getCategoryId())) {
            categoryDao.save(categoryDO);
        } else {
            categoryDO.setId(categoryReq.getCategoryId());
            categoryDao.updateById(categoryDO);
        }
        categoryService.refreshCache();
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        CategoryDO categoryDO = categoryDao.getById(categoryId);
        if (categoryDO != null) {
            categoryDao.removeById(categoryDO);
        }
        categoryService.refreshCache();
    }

    @Override
    public void operateCategory(Integer categoryId, Integer pushStatus) {
        CategoryDO categoryDO = categoryDao.getById(categoryId);
        if (categoryDO != null) {
            categoryDO.setStatus(pushStatus);
            categoryDao.updateById(categoryDO);
        }
        categoryService.refreshCache();
    }

    @Override
    public PageVo<CategoryDTO> getCategoryList(PageParam pageParam) {
        List<CategoryDTO> tagDTOS = categoryDao.listCategory(pageParam);
        Integer totalCount = categoryDao.countCategory();
        return PageVo.build(tagDTOS, pageParam.getPageSize(), pageParam.getPageNum(), totalCount);
    }
}

