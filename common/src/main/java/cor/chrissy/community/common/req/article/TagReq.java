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
    private String tagName;

    /**
     * 标签类型：1-系统标签，2-自定义标签
     */
    private Integer tagType;

    /**
     * 类目ID
     */
    private Long categoryId;
}
