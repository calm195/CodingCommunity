package cor.chrissy.community.service.banner.dto;

import cor.chrissy.community.common.entity.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BannerDTO extends BaseDTO {

    /**
     * 图片名称
     */
    private String bannerName;

    /**
     * 图片url
     */
    private String bannerUrl;

    /**
     * 图片类型
     */
    private Integer bannerType;

    /**
     * 排序
     */
    private Integer rank;

    /**
     * 状态：0-未发布，1-已发布
     */
    private Integer status;
}

