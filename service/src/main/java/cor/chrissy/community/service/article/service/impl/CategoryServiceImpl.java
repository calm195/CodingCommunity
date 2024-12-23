package cor.chrissy.community.service.article.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.repository.dao.CategoryDao;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;
import cor.chrissy.community.service.article.service.CategoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    /**
     * 分类数一般不会特别多，如编程领域可以预期的分类将不会超过30，所以可以做一个全量的内存缓存
     * todo 后续可改为Guava -> Redis
     */
    private LoadingCache<Long, CategoryDTO> categoryCaches;

    private CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @PostConstruct
    public void init() {
        categoryCaches = CacheBuilder.newBuilder().maximumSize(300).build(new CacheLoader<Long, CategoryDTO>() {
            @Override
            public CategoryDTO load(@NotNull Long categoryId) throws Exception {
                CategoryDO category = categoryDao.getById(categoryId);
                if (category == null || category.getDeleted() == YesOrNoEnum.YES.getCode()) {
                    return CategoryDTO.EMPTY;
                }
                return new CategoryDTO(categoryId, category.getCategoryName());
            }
        });
        // 预热全量缓存
        refreshCache();
    }

    /**
     * 查询类目名
     *
     * @param categoryId
     * @return
     */
    @Override
    public String queryCategoryName(Long categoryId) {
        return categoryCaches.getUnchecked(categoryId).getCategory();
    }

    /**
     * 查询所有的分类
     *
     * @return
     */
    public List<CategoryDTO> loadAllCategories() {
        List<CategoryDTO> list = new ArrayList<>(categoryCaches.asMap().values());
        list.sort(Comparator.comparingLong(CategoryDTO::getCategoryId));
        return list;
    }

    /**
     * 刷新缓存
     */
    private void refreshCache() {
        List<CategoryDO> list = categoryDao.listAllCategoriesFromDb();
        categoryCaches.invalidateAll();
        categoryCaches.cleanUp();
        list.forEach(s -> categoryCaches.put(s.getId(), ArticleConverter.toDto(s)));
    }

    @Override
    public Long queryCategoryId(String category) {
        return categoryCaches.asMap().values().stream()
                .filter(s -> s.getCategory().equalsIgnoreCase(category))
                .findFirst().map(CategoryDTO::getCategoryId).orElse(null);
    }
}

