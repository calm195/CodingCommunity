package cor.chrissy.community.common.req.article;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wx128
 * @createAt 2025/1/2
 */
@Data
public class ContentPostReq implements Serializable {
    /**
     * 正文内容
     */
    private String content;
}
