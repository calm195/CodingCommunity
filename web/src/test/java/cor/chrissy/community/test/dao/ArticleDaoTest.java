package cor.chrissy.community.test.dao;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.repository.dao.CategoryDao;
import cor.chrissy.community.service.article.repository.dao.TagDao;
import cor.chrissy.community.service.article.repository.entity.CategoryDO;
import cor.chrissy.community.service.article.repository.entity.TagDO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.test.BasicTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Slf4j
public class ArticleDaoTest extends BasicTest {
    @Autowired
    private TagDao tagDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ArticleReadService articleService;

    @Test
    public void testCategory() {
        CategoryDO category = new CategoryDO();
        category.setCategoryName("后端");
        category.setStatus(1);
        categoryDao.save(category);
        log.info("save category:{} -> id:{}", category, category.getId());
    }

    @Test
    public void testTag() {
        TagDO tag = new TagDO();
        tag.setTagName("Java");
        tag.setTagType(1);
//        tag.setCategoryId(1L);
        tagDao.save(tag);
        log.info("tagId: {}", tag.getId());
    }

    @Test
    public void testArticle() {
        PageListVo<ArticleDTO> articleListDTO = articleService.queryArticlesByCategory(1L, PageParam.newPageInstance(1L, 10L));
        log.info("articleListDTO: {}", articleListDTO);
    }
}
