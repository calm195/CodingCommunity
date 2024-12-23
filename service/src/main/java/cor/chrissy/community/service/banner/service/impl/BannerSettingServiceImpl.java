package cor.chrissy.community.service.banner.service.impl;

import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.banner.BannerReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.banner.converter.BannerConverter;
import cor.chrissy.community.service.banner.dto.BannerDTO;
import cor.chrissy.community.service.banner.repository.dao.BannerDao;
import cor.chrissy.community.service.banner.repository.entity.BannerDO;
import cor.chrissy.community.service.banner.service.BannerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class BannerSettingServiceImpl implements BannerSettingService {

    @Autowired
    private BannerDao bannerDao;

    @Override
    public void saveBanner(BannerReq bannerReq) {
        BannerDO bannerDO = BannerConverter.ToDO(bannerReq);
        if (NumUtil.nullOrZero(bannerReq.getBannerId())) {
            bannerDao.save(bannerDO);
        } else {
            bannerDO.setId(bannerReq.getBannerId());
            bannerDao.updateById(bannerDO);
        }
    }

    @Override
    public void deleteBanner(Integer bannerId) {
        BannerDO bannerDO = bannerDao.getById(bannerId);
        if (bannerDO != null){
            bannerDO.setDeleted(YesOrNoEnum.YES.getCode());
            bannerDao.updateById(bannerDO);
        }
    }

    @Override
    public void operateBanner(Integer bannerId, Integer operateType) {
        BannerDO bannerDO = bannerDao.getById(bannerId);
        if (bannerDO != null){
            bannerDO.setStatus(operateType);
            bannerDao.updateById(bannerDO);
        }
    }

    @Override
    public PageVo<BannerDTO> getBannerList(PageParam pageParam) {
        List<BannerDTO> bannerDTOS = bannerDao.listBanner(pageParam);
        Integer totalCount = bannerDao.countBanner();
        return PageVo.build(bannerDTOS, pageParam.getPageSize(), pageParam.getPageNum(), totalCount);
    }
}

