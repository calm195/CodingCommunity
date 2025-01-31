package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.TagReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
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
@Permission(role = UserRole.LOGIN)
@RequestMapping(path = {"/api/admin/tag", "/admin/tag"})
public class TagSettingRestController {

    @Autowired
    private TagSettingService tagSettingService;

    @ResponseBody
    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public Result<String> save(@RequestBody TagReq req) {
        tagSettingService.saveTag(req);
        return Result.ok("ok");
    }

    @ResponseBody
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public Result<String> delete(@RequestParam(name = "tagId") Integer tagId) {
        tagSettingService.deleteTag(tagId);
        return Result.ok("ok");
    }

    @ResponseBody
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public Result<String> operate(@RequestParam(name = "tagId") Integer tagId,
                                  @RequestParam(name = "pushStatus") Integer pushStatus) {
        if (pushStatus != PushStatEnum.OFFLINE.getCode() || pushStatus != PushStatEnum.ONLINE.getCode()) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        tagSettingService.operateTag(tagId, pushStatus);
        return Result.ok("ok");
    }

    @ResponseBody
    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "list")
    public Result<PageVo<TagDTO>> list(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                       @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<TagDTO> tagDTOPageVo = tagSettingService.getTagList(PageParam.newPageInstance(pageNumber, pageSize));
        return Result.ok(tagDTOPageVo);
    }
}
