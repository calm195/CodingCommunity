package cor.chrissy.community.service.article.dto;

import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class ColumnDTO {

    /**
     * 专栏id
     */
    private Long columnId;

    /**
     * 专栏名
     */
    private String column;

    /**
     * 说明
     */
    private String introduction;

    /**
     * 封面
     */
    private String cover;

    /**
     * 发布时间
     */
    private Long publishTime;

    /**
     * 0 未发布 1 连载 2 完结
     *
     * @see cor.chrissy.community.common.enums.ColumnStatusEnum#getCode()
     */
    private Integer state;

    /**
     * 作者
     */
    private Long author;

    /**
     * 作者名
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 个人简介
     */
    private String authorProfile;

    /**
     * 统计计数相关信息
     */
    private ColumnFootCountDTO count;
}
