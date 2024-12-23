package cor.chrissy.community.common.req.banner;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class BannerReq implements Serializable {

    /**
     * ID
     */
    private Long bannerId;

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
}
