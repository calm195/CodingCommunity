package cor.chrissy.community.web.backstage.view;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.service.TagSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "backstage/tag/")
public class TagSettingViewController {

    @Autowired
    private TagSettingService tagSettingService;

    @ResponseBody
    @GetMapping(path = "list")
    public Result<PageVo<TagDTO>> list(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                       @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<TagDTO> tagDTOPageVo = tagSettingService.getTagList(PageParam.newPageInstance(pageNumber.longValue(), pageSize.longValue()));
        return Result.ok(tagDTOPageVo);
    }
}
