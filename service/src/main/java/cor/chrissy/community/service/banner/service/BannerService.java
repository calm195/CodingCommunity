package cor.chrissy.community.service.banner.service;

import cor.chrissy.community.common.enums.BannerTypeEnum;
import cor.chrissy.community.service.banner.dto.BannerDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface BannerService {
    /**
     * 获取 Banner 列表
     *
     * @param bannerTypeEnum
     * @return
     */
    List<BannerDTO> getBannerList(BannerTypeEnum bannerTypeEnum);
}
