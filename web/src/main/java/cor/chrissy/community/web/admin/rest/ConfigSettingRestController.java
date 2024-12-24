package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.banner.ConfigReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.config.service.ConfigSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "admin/config/")
public class ConfigSettingRestController {

    @Autowired
    private ConfigSettingService configSettingService;

    @ResponseBody
    @PostMapping(path = "save")
    public Result<String> save(@RequestBody ConfigReq configReq) {
        configSettingService.saveConfig(configReq);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "delete")
    public Result<String> delete(@RequestParam(name = "configId") Integer configId) {
        configSettingService.deleteConfig(configId);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "operate")
    public Result<String> operate(@RequestParam(name = "configId") Integer configId,
                                 @RequestParam(name = "operateType") Integer operateType) {
        if (operateType != PushStatEnum.OFFLINE.getCode() && operateType!= PushStatEnum.ONLINE.getCode()) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        configSettingService.operateConfig(configId, operateType);
        return Result.ok("ok");
    }
}

