package cor.chrissy.community.common.req.article;

import lombok.Data;

import java.io.Serializable;

/**
 * Category保存请求参数
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class CategoryReq implements Serializable {

    /**
     * ID
     */
    private Long categoryId;

    /**
     * 类目名称
     */
    private String category;

    private Integer rank;
}
