package cor.chrissy.community.common.req.article;

import lombok.Data;

import java.io.Serializable;

/**
 * Tag 保存请求参数
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class TagReq implements Serializable {

    /**
     * ID
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private String tag;

    /**
     * 类目ID
     */
    private Long categoryId;
}
