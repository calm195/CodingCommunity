package cor.chrissy.community.common.req;

import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/10
 */
@Data
public class PageSearchReq {

    /**
     * 页数
     */
    private Long pageSize;

    /**
     * 页码
     */
    private Long pageNum;
}