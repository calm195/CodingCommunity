package cor.chrissy.community.web.backstage.view;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.banner.dto.BannerDTO;
import cor.chrissy.community.service.banner.service.BannerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "backstage/banner/")
public class BannerSettingViewController {

    @Autowired
    private BannerSettingService bannerSettingService;

    @ResponseBody
    @GetMapping(path = "list")
    public Result<PageVo<BannerDTO>> list(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                          @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<BannerDTO> bannerDTOPageVo = bannerSettingService.getBannerList(PageParam.newPageInstance(pageNumber.longValue(), pageSize.longValue()));
        return Result.ok(bannerDTOPageVo);
    }
}
