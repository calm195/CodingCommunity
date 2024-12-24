package cor.chrissy.community.web.common;

import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.config.service.DictCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
@RestController
@RequestMapping(path = "common/")
public class DictCommonController {

    @Autowired
    private DictCommonService dictCommonService;

    @ResponseBody
    @GetMapping(path = "/dict")
    public Result<Map<String, Object>> list() {
        Map<String, Object> bannerDTOPageVo = dictCommonService.getDict();
        return Result.ok(bannerDTOPageVo);
    }
}
