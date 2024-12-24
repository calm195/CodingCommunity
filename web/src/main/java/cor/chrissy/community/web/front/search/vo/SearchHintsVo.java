package cor.chrissy.community.web.front.search.vo;

import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
@Data
public class SearchHintsVo implements Serializable {
    private static final long serialVersionUID = -2989169905031769195L;
    /**
     * 搜索的关键词
     */
    private String key;

    private List<SimpleArticleDTO> items;
}
