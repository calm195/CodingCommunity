package cor.chrissy.community.service.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import cor.chrissy.community.common.enums.PushStatusEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;
import cor.chrissy.community.service.article.repository.mapper.CategoryMapper;
import cor.chrissy.community.service.article.service.CategoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    /**
     * 分类数一般不会特别多，如编程领域可以预期的分类将不会超过30，所以可以做一个全量的内存缓存
     * fixme: 现在暂时使用ArrayList存储；后续可改为Guava -> Redis
     */
    private LoadingCache<Long, CategoryDTO> categoryCaches;

    @Resource
    private CategoryMapper categoryMapper;

    @PostConstruct
    public void init() {
        categoryCaches = CacheBuilder.newBuilder().maximumSize(300).build(new CacheLoader<Long, CategoryDTO>() {
            @NotNull
            @Override
            public CategoryDTO load(@NotNull Long categoryId) throws Exception {
                CategoryDO category = categoryMapper.selectById(categoryId);
                if (category == null || category.getDeleted() == YesOrNoEnum.YES.getCode()) {
                    return CategoryDTO.EMPTY;
                }
                return new CategoryDTO(categoryId, category.getCategoryName());
            }
        });
    }

    /**
     * 查询类目名
     *
     * @param categoryId
     * @return
     */
    @Override
    public String getCategoryName(Long categoryId) {
        return categoryCaches.getUnchecked(categoryId).getCategory();
    }

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

    /**
     * 查询所有的分类
     *
     * @param forceDB
     * @return
     */
    public List<CategoryDTO> loadAllCategories(boolean forceDB) {
        if (forceDB || categoryCaches == null) {
            List<CategoryDTO> list = loadAllCategoriesFromDb();
            categoryCaches.invalidateAll();
            categoryCaches.cleanUp();
            list.forEach(s -> categoryCaches.put(s.getCategoryId(), s));
            return list;
        } else {
            return new ArrayList<>(categoryCaches.asMap().values());
        }
    }

    private List<CategoryDTO> loadAllCategoriesFromDb() {
        LambdaQueryWrapper<CategoryDO> query = Wrappers.lambdaQuery();
        query.eq(CategoryDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(CategoryDO::getStatus, PushStatusEnum.ONLINE.getCode());
        List<CategoryDO> records = categoryMapper.selectList(query);
        return records.stream().map(s -> new CategoryDTO(s.getId(), s.getCategoryName())).collect(Collectors.toList());
    }
}

