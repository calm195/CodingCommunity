package cor.chrissy.community.common.req.banner;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class ConfigReq implements Serializable {

    /**
     * ID
     */
    private Long configId;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 图片url
     */
    private String bannerUrl;

    /**
     * 图片类型
     */
    private Integer type;

    private String jumpUrl;

    private String content;

    /**
     * 排序
     */
    private Integer rank;

    private String tags;
}
