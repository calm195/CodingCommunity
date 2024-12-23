package cor.chrissy.community.common.req.article;

import lombok.Data;

import java.io.Serializable;

/**
 * Column 保存请求参数
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class ColumnReq implements Serializable {

    /**
     * ID
     */
    private Long columnId;

    /**
     * 专栏名
     */
    private String columnName;

    /**
     * 作者
     */
    private Long userId;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 封面
     */
    private String cover;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 上线时间
     */
    private Long publishTime;
}

