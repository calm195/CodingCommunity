package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.config.service.DictCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wx128
 * @createAt 2025/1/4
 */
@RestController
@Permission(role = UserRole.ADMIN)
@RequestMapping(path = "admin/common/")
public class CommonSettingRestController {
    @Autowired
    private DictCommonService dictCommonService;

    @ResponseBody
    @GetMapping(path = "/dict")
    public Result<Map<String, Object>> list() {
        Map<String, Object> bannerDTOPageVo = dictCommonService.getDict();
        return Result.ok(bannerDTOPageVo);
    }
}
