package cor.chrissy.community.service.banner.service.impl;

import cor.chrissy.community.common.enums.BannerTypeEnum;
import cor.chrissy.community.service.banner.dto.BannerDTO;
import cor.chrissy.community.service.banner.repository.dao.BannerDao;
import cor.chrissy.community.service.banner.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerDao bannerDao;

    @Override
    public List<BannerDTO> getBannerList(BannerTypeEnum bannerTypeEnum) {
        return bannerDao.listBannerByBannerType(bannerTypeEnum.getCode());
    }
}
