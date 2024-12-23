package cor.chrissy.community.service.banner.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.banner.converter.BannerConverter;
import cor.chrissy.community.service.banner.dto.BannerDTO;
import cor.chrissy.community.service.banner.repository.entity.BannerDO;
import cor.chrissy.community.service.banner.repository.mapper.BannerMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Repository
public class BannerDao extends ServiceImpl<BannerMapper, BannerDO> {

    /**
     * 根据类型获取 Banner 列表（无需分页）
     *
     * @param bannerType
     * @return
     */
    public List<BannerDTO> listBannerByBannerType(Integer bannerType) {
        List<BannerDO> bannerDOS = lambdaQuery()
                .eq(BannerDO::getBannerType, bannerType)
                .eq(BannerDO::getStatus, PushStatEnum.ONLINE.getCode())
                .eq(BannerDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByAsc(BannerDO::getRank)
                .list();
        return BannerConverter.ToDTOS(bannerDOS);
    }

    /**
     * 获取所有 Banner 列表（分页）
     *
     * @return
     */
    public List<BannerDTO> listBanner(PageParam pageParam) {
        List<BannerDO> bannerDOS = lambdaQuery()
                .eq(BannerDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(BannerDO::getCreateTime)
                .last(PageParam.getLimitSql(pageParam))
                .list();
        return BannerConverter.ToDTOS(bannerDOS);
    }

    /**
     * 获取所有 Banner 总数（分页）
     *
     * @return
     */
    public Integer countBanner() {
        return lambdaQuery()
                .eq(BannerDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count()
                .intValue();
    }
}

