package cor.chrissy.community.test.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.dto.ArticleListDTO;
import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;
import cor.chrissy.community.service.article.repository.entity.TagDO;
import cor.chrissy.community.service.article.service.ArticleRepository;
import cor.chrissy.community.service.article.service.ArticleService;
import cor.chrissy.community.service.article.service.CategoryService;
import cor.chrissy.community.service.article.service.TagService;
import cor.chrissy.community.test.BasicTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Slf4j
public class ArticleDaoTest extends BasicTest {
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testCategory() {
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
        ArticleListDTO articleListDTO = articleService.getCollectionArticleListByUserId(1L, PageParam.newPageInstance(1L, 10L));
        log.info("articleListDTO: {}", articleListDTO);
    }

    @Test
    public void testSaveArticle() {
        Set<Long> set = new HashSet<>();
        set.add(1L);
        ArticleDO article = new ArticleDO();
        article.setAuthorId(1L);
        article.setCategoryId(1L);
        article.setArticleType(1);
        article.setShortTitle("test short title");
        article.setPicture("test picture");
        article.setCategoryId(1L);
        article.setSource(1);
        article.setSourceUrl("test url");
        article.setTitle("test title");
        article.setSummary("test summary");
        articleRepository.saveArticle(article, "test content", set);
    }
}
