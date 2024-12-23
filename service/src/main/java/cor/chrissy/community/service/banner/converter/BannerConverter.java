package cor.chrissy.community.service.banner.converter;

import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.req.banner.BannerReq;
import cor.chrissy.community.service.banner.dto.BannerDTO;
import cor.chrissy.community.service.banner.repository.entity.BannerDO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public class BannerConverter {

    public static List<BannerDTO> ToDTOS(List<BannerDO> bannerDOS) {
        if (CollectionUtils.isEmpty(bannerDOS)){
            return Collections.emptyList();
        }
        List<BannerDTO> bannerDTOS = new ArrayList<>(bannerDOS.size());
        bannerDOS.forEach( v -> bannerDTOS.add(ToDTO(v)));
        return bannerDTOS;
    }

    public static BannerDTO ToDTO(BannerDO bannerDO) {
        if (bannerDO == null) {
            return null;
        }
        BannerDTO bannerDTO = new BannerDTO();
        bannerDTO.setBannerName(bannerDO.getBannerName());
        bannerDTO.setBannerUrl(bannerDO.getBannerUrl());
        bannerDTO.setBannerType(bannerDO.getBannerType());
        bannerDTO.setRank(bannerDO.getRank());
        bannerDTO.setStatus(bannerDO.getStatus());
        bannerDTO.setId(bannerDO.getId());
        bannerDTO.setCreateTime(bannerDO.getCreateTime());
        bannerDTO.setUpdateTime(bannerDO.getUpdateTime());
        return bannerDTO;
    }

    public static BannerDO ToDO(BannerReq bannerReq) {
        if (bannerReq == null) {
            return null;
        }
        BannerDO bannerDO = new BannerDO();
        bannerDO.setBannerName(bannerReq.getBannerName());
        bannerDO.setBannerUrl(bannerReq.getBannerUrl());
        bannerDO.setBannerType(bannerReq.getBannerType());
        bannerDO.setRank(bannerReq.getRank());
        bannerDO.setStatus(PushStatEnum.OFFLINE.getCode());
        return bannerDO;
    }
}
