package cor.chrissy.community.common.req.article;

import lombok.Data;

import java.io.Serializable;

/**
 * Column 保存文章请求参数
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class ColumnArticleReq implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 专栏ID
     */
    private Long columnId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 顺序，越小越靠前 todo: rename
     */
    private Integer section;
}
