package cor.chrissy.community.service.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cor.chrissy.community.core.enums.PushStatusEnum;
import cor.chrissy.community.core.enums.YesOrNoEnum;
import cor.chrissy.community.core.req.PageParam;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;
import cor.chrissy.community.service.article.repository.mapper.CategoryMapper;
import cor.chrissy.community.service.article.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wx128
 * @date 2024/12/9
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Long addCategory(CategoryDO categoryDTO) {
        categoryMapper.insert(categoryDTO);
        return categoryDTO.getId();
    }

    @Override
    public void updateCategory(Long categoryId, String categoryName) {
        CategoryDO categoryDTO = categoryMapper.selectById(categoryId);
        if (categoryDTO != null) {
            categoryDTO.setCategoryName(categoryName);
            categoryDTO.setStatus(YesOrNoEnum.NO.getCode());
            categoryMapper.updateById(categoryDTO);
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        CategoryDO categoryDTO = categoryMapper.selectById(categoryId);
        if (categoryDTO != null) {
            categoryDTO.setDeleted(YesOrNoEnum.YES.getCode());
            categoryMapper.updateById(categoryDTO);
        }
    }

    @Override
    public void operateCategory(Long categoryId, PushStatusEnum pushStatusEnum) {
        CategoryDO categoryDTO = categoryMapper.selectById(categoryId);
        if (categoryDTO != null) {
            categoryDTO.setStatus(pushStatusEnum.getCode());
            categoryMapper.updateById(categoryDTO);
        }
    }

    @Override
    public IPage<CategoryDO> getCategoryByPage(PageParam pageParam) {
        LambdaQueryWrapper<CategoryDO> query = Wrappers.lambdaQuery();
        query.eq(CategoryDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(CategoryDO::getStatus, PushStatusEnum.ONLINE.getCode());
        Page<CategoryDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        return categoryMapper.selectPage(page, query);
    }
}
