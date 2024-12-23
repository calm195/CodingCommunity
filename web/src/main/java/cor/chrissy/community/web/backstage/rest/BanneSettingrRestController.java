package cor.chrissy.community.web.backstage.rest;

import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.banner.BannerReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.banner.service.BannerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "backstage/banner/")
public class BanneSettingrRestController {

    @Autowired
    private BannerSettingService bannerSettingService;

    @ResponseBody
    @PostMapping(path = "save")
    public Result<String> save(@RequestBody BannerReq bannerReq) {
        bannerSettingService.saveBanner(bannerReq);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "delete")
    public Result<String> delete(@RequestParam(name = "bannerId") Integer bannerId) {
        bannerSettingService.deleteBanner(bannerId);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "operate")
    public Result<String> operate(@RequestParam(name = "bannerId") Integer bannerId,
                                 @RequestParam(name = "operateType") Integer operateType) {
        if (operateType != PushStatEnum.OFFLINE.getCode() && operateType!= PushStatEnum.ONLINE.getCode()) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        bannerSettingService.operateBanner(bannerId, operateType);
        return Result.ok("ok");
    }
}

