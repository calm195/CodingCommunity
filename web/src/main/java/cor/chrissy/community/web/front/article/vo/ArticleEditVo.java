package cor.chrissy.community.web.front.article.vo;

import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.dto.TagDTO;
import lombok.Data;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Data
public class ArticleEditVo {

    private ArticleDTO article;

    private List<CategoryDTO> categories;

    private List<TagDTO> tags;

}