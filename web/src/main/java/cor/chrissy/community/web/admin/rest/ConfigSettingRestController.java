package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.banner.ConfigReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.config.dto.ConfigDTO;
import cor.chrissy.community.service.config.service.ConfigSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@Permission(role = UserRole.LOGIN)
@RequestMapping(path = {"/api/admin/config", "/admin/config/"})
public class ConfigSettingRestController {

    @Autowired
    private ConfigSettingService configSettingService;

    @ResponseBody
    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public Result<String> save(@RequestBody ConfigReq configReq) {
        configSettingService.saveConfig(configReq);
        return Result.ok("ok");
    }

    @ResponseBody
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public Result<String> delete(@RequestParam(name = "configId") Integer configId) {
        configSettingService.deleteConfig(configId);
        return Result.ok("ok");
    }

    @ResponseBody
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public Result<String> operate(@RequestParam(name = "configId") Integer configId,
                                  @RequestParam(name = "operateType") Integer operateType) {
        if (operateType != PushStatEnum.OFFLINE.getCode() && operateType != PushStatEnum.ONLINE.getCode()) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        configSettingService.operateConfig(configId, operateType);
        return Result.ok("ok");
    }

    @ResponseBody
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "list")
    public Result<PageVo<ConfigDTO>> list(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                          @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<ConfigDTO> bannerDTOPageVo = configSettingService.getConfigList(PageParam.newPageInstance(pageNumber, pageSize));
        return Result.ok(bannerDTOPageVo);
    }
}

