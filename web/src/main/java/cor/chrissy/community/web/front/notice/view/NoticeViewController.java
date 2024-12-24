package cor.chrissy.community.web.front.notice.view;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.NotifyTypeEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.notify.service.NotifyService;
import cor.chrissy.community.web.front.notice.vo.NoticeResVo;
import cor.chrissy.community.web.global.BaseViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Controller
@Permission(role = UserRole.LOGIN)
@RequestMapping(path = "notice")
public class NoticeViewController extends BaseViewController {
    @Autowired
    private NotifyService notifyService;

    @RequestMapping({"/{type}", "/"})
    public String list(@PathVariable(name = "type", required = false) String type, Model model) {

        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        Map<String, Integer> map = notifyService.queryUnreadCounts(loginUserId);

        NotifyTypeEnum typeEnum = type == null ? null : NotifyTypeEnum.typeOf(type);
        if (typeEnum == null) {
            typeEnum = map.entrySet().stream()
                    .filter(s -> s.getValue() > 0)
                    .map(s -> NotifyTypeEnum.typeOf(s.getKey()))
                    .findAny()
                    .orElse(NotifyTypeEnum.COMMENT);
        }

        NoticeResVo vo = new NoticeResVo();
        vo.setList(notifyService.queryUserNotices(loginUserId, typeEnum, PageParam.newPageInstance()));

        vo.setSelectType(typeEnum.name().toLowerCase());
        vo.setUnreadCountMap(notifyService.queryUnreadCounts(loginUserId));
        model.addAttribute("vo", vo);
        return "views/notice/index";
    }
}
