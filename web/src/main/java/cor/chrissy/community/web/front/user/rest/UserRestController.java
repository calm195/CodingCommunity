package cor.chrissy.community.web.front.user.rest;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.FollowTypeEnum;
import cor.chrissy.community.common.enums.HomeSelectEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserRelationReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.NextPageHtmlVo;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.core.util.ExceptionUtil;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.user.dto.FollowUserInfoDTO;
import cor.chrissy.community.service.user.service.UserRelationService;
import cor.chrissy.community.service.user.service.UserService;
import cor.chrissy.community.web.component.TemplateEngineHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "user/api")
public class UserRestController {

    @Resource
    private UserService userService;

    @Resource
    private UserRelationService userRelationService;

    @Resource
    private TemplateEngineHelper templateEngineHelper;

    @Resource
    private ArticleReadService articleReadService;

    /**
     * 保存用户关系
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "saveUserRelation")
    public Result<Boolean> saveUserRelation(@RequestBody UserRelationReq req) {
        userRelationService.saveUserRelation(req);
        return Result.ok(true);
    }

    /**
     * 保存用户详情
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "saveUserInfo")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> saveUserInfo(@RequestBody UserInfoSaveReq req) {
        if (!(req.getUserId() != null && req.getUserId().equals(ReqInfoContext.getReqInfo().getUserId()))) {
            // 不能修改其他用户的信息
            throw ExceptionUtil.of(StatusEnum.FORBID_ERROR_MIXED, "无权修改");
        }
        userService.saveUserInfo(req);
        return Result.ok(true);
    }

    /**
     * 用户的文章列表翻页
     *
     * @param userId
     * @param homeSelectType
     * @return
     */
    @GetMapping(path = "articleList")
    public Result<NextPageHtmlVo> articleList(@RequestParam(name = "userId") Long userId,
                                              @RequestParam(name = "homeSelectType") String homeSelectType,
                                              @RequestParam("page") Long page,
                                              @RequestParam(name = "pageSize", required = false) Long pageSize) {
        HomeSelectEnum select = HomeSelectEnum.fromCode(homeSelectType);
        if (select == null) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }

        if (pageSize == null) pageSize = PageParam.DEFAULT_PAGE_SIZE;
        PageParam pageParam = PageParam.newPageInstance(page, pageSize);
        PageListVo<ArticleDTO> dto = articleReadService.queryArticlesByUserAndType(userId, pageParam, select);
        String html = templateEngineHelper.renderToVo("views/user/articles/index", "homeSelectList", dto);
        return Result.ok(new NextPageHtmlVo(html, dto.getHasMore()));
    }

    @GetMapping(path = "followList")
    public Result<NextPageHtmlVo> followList(@RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "followSelectType") String followSelectType,
                                             @RequestParam("page") Long page,
                                             @RequestParam(name = "pageSize", required = false) Long pageSize) {
        if (pageSize == null) pageSize = PageParam.DEFAULT_PAGE_SIZE;
        PageParam pageParam = PageParam.newPageInstance(page, pageSize);
        PageListVo<FollowUserInfoDTO> followList;
        boolean needUpdateRelation = false;
        if (followSelectType.equals(FollowTypeEnum.FOLLOW.getCode())) {
            followList = userRelationService.getUserFollowList(userId, pageParam);
        } else {
            // 查询粉丝列表时，只能确定粉丝关注了userId，但是不能反向判断，因此需要再更新下映射关系，判断userId是否有关注这个用户
            followList = userRelationService.getUserFansList(userId, pageParam);
            needUpdateRelation = true;
        }

        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        if (!Objects.equals(loginUserId, userId) || needUpdateRelation) {
            userRelationService.updateUserFollowRelationId(followList, userId);
        }
        String html = templateEngineHelper.renderToVo("views/user/follows/index", "followList", followList);
        return Result.ok(new NextPageHtmlVo(html, followList.getHasMore()));
    }
}

