package cor.chrissy.community.service.config.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import cor.chrissy.community.common.entity.BaseDO;
import cor.chrissy.community.common.enums.ConfigTagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("config")
public class ConfigDO extends BaseDO {
    private static final long serialVersionUID = -6122208316544171303L;
    /**
     * 类型
     */
    private Integer type;

    /**
     * 名称
     */
    @TableField("`name`")
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
    @TableField("`rank`")
    private Integer rank;

    /**
     * 状态：0-未发布，1-已发布
     */
    private Integer status;

    /**
     * 0未删除 1 已删除
     */
    private Integer deleted;

    /**
     * 配置对应的标签，英文逗号分隔
     *
     * @see ConfigTagEnum#getCode()
     */
    private String tags;
}
