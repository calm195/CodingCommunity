package cor.chrissy.community.web.front.notice.rest;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.NotifyTypeEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.NextPageHtmlVo;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.core.util.ExceptionUtil;
import cor.chrissy.community.service.notify.dto.NotifyMsgDTO;
import cor.chrissy.community.service.notify.service.NotifyService;
import cor.chrissy.community.web.component.TemplateEngineHelper;
import cor.chrissy.community.web.front.notice.vo.NoticeResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Permission(role = UserRole.LOGIN)
@RestController
@RequestMapping(path = "notice/api")
public class NoticeRestController {
    @Autowired
    private TemplateEngineHelper templateEngineHelper;

    private NotifyService notifyService;

    public NoticeRestController(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    private PageListVo<NotifyMsgDTO> listItems(String type, Long page, Long pageSize) {
        NotifyTypeEnum typeEnum = NotifyTypeEnum.typeOf(type);
        if (typeEnum == null) {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "type" + type + "非法");
        }
        if (pageSize == null) {
            pageSize = PageParam.DEFAULT_PAGE_SIZE;
        }
        return notifyService.queryUserNotices(ReqInfoContext.getReqInfo().getUserId(),
                typeEnum, PageParam.newPageInstance(page, pageSize));

    }

    /**
     * 消息通知列表，用于前后端分离的场景
     *
     * @param type     @link NotifyTypeEnum
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(path = "list")
    public Result<PageListVo<NotifyMsgDTO>> list(@RequestParam(name = "type") String type,
                                                 @RequestParam("page") Long page,
                                                 @RequestParam(name = "pageSize", required = false) Long pageSize) {
        return Result.ok(listItems(type, page, pageSize));
    }

    /**
     * 返回渲染好的分页信息
     *
     * @param type
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(path = "items")
    public Result<NextPageHtmlVo> listForView(@RequestParam(name = "type") String type,
                                              @RequestParam("page") Long page,
                                              @RequestParam(name = "pageSize", required = false) Long pageSize) {
        type = type.toLowerCase().trim();
        PageListVo<NotifyMsgDTO> list = listItems(type, page, pageSize);
        NoticeResVo vo = new NoticeResVo();
        vo.setList(list);
        vo.setSelectType(type);
        String html = templateEngineHelper.render("views/notice/notify-" + type, vo);
        return Result.ok(new NextPageHtmlVo(html, list.getHasMore()));
    }
}

