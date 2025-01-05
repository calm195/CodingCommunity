package cor.chrissy.community.service.article.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
@Accessors(chain = true)
public class SimpleArticleDTO implements Serializable {
    private static final long serialVersionUID = 3646376715620165839L;

    private Long id;

    private String title;

    private Timestamp createTime;

    /**
     * 专栏ID
     */
    private Long columnId;

    /**
     * 专栏标题
     */
    private String column;

    /**
     * 文章排序
     */
    private Integer sort;
}
