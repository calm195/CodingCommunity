package cor.chrissy.community.service.config.dto;

import cor.chrissy.community.common.entity.BaseDTO;
import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/23
 */
@Data
public class ConfigDTO extends BaseDTO {

    /**
     * 类型
     */
    private Integer type;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片链接
     */
    private String bannerUrl;

    /**
     * 跳转链接
     */
    private String jumpUrl;

    /**
     * 内容
     */
    private String content;

    /**
     * 排序
     */
    private Integer rank;

    /**
     * 状态：0-未发布，1-已发布
     */
    private Integer status;

    /**
     * 配置相关的标签：如 火，推荐，精选 等等，英文逗号分隔
     *
     * @see cor.chrissy.community.common.enums.ConfigTagEnum#getCode()
     */
    private String tags;
}

