package cor.chrissy.community.service.banner.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.banner.BannerReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.service.banner.dto.BannerDTO;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface BannerSettingService {
    /**
     * 保存
     *
     * @param bannerReq
     */
    void saveBanner(BannerReq bannerReq);

    /**
     * 删除
     *
     * @param bannerId
     */
    void deleteBanner(Integer bannerId);

    /**
     * 操作（上线/下线）
     *
     * @param bannerId
     */
    void operateBanner(Integer bannerId, Integer operateType);

    /**
     * 获取 Banner 列表
     *
     * @param pageParam
     * @return
     */
    PageVo<BannerDTO> getBannerList(PageParam pageParam);
}
