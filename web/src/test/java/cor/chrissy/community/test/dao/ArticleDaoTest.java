package cor.chrissy.community.test.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;
import cor.chrissy.community.service.article.repository.entity.TagDO;
import cor.chrissy.community.service.article.service.impl.ArticleServiceImpl;
import cor.chrissy.community.service.article.service.impl.CategoryServiceImpl;
import cor.chrissy.community.service.article.service.impl.TagServiceImpl;
import cor.chrissy.community.test.BasicTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Slf4j
public class ArticleDaoTest extends BasicTest {
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private ArticleServiceImpl articleService;
    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    public void testCategory(){
        CategoryDO category = new CategoryDO();
        category.setCategoryName("后端");
        category.setStatus(1);
        Long categoryId = categoryService.addCategory(category);
        log.info("save category:{} -> id:{}", category, categoryId);

        IPage<CategoryDO> list = categoryService.getCategoryByPage(PageParam.newPageInstance(0L, 10L));
        log.info("query list: {}", list.getRecords());
    }

    @Test
    public void testTag() {
        TagDO tag = new TagDO();
        tag.setTagName("Java");
        tag.setTagType(1);
        tag.setCategoryId(1L);
        Long tagId = tagService.addTag(tag);
        log.info("tagId: {}", tagId);

        List<TagDTO> list = tagService.getTagListByCategoryId(1L);
        log.info("tagList: {}", list);
    }

    @Test
    public void testArticle() {

    }
}
