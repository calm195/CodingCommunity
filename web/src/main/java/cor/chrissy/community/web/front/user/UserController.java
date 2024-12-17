package cor.chrissy.community.web.front.user;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.FollowSelectEnum;
import cor.chrissy.community.common.enums.FollowTypeEnum;
import cor.chrissy.community.common.enums.HomeSelectEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserRelationReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.article.dto.ArticleListDTO;
import cor.chrissy.community.service.article.dto.TagSelectDTO;
import cor.chrissy.community.service.article.service.ArticleService;
import cor.chrissy.community.service.user.dto.UserFollowListDTO;
import cor.chrissy.community.service.user.dto.UserHomeDTO;
import cor.chrissy.community.service.user.service.UserRelationService;
import cor.chrissy.community.service.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Controller
@Permission(role = UserRole.LOGIN)
@RequestMapping(path = "user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private UserRelationService userRelationService;

    @Resource
    private ArticleService articleService;

    private static final List<String> homeSelectTags = Arrays.asList("article", "read", "follow", "collection");
    private static final List<String> followSelectTags = Arrays.asList("follow", "fans");

    /**
     * 保存用户
     *
     * @param req
     * @return
     */
    @PostMapping(path = "saveUser")
    public String saveUser(UserSaveReq req) {
        userService.saveUser(req);
        return "";
    }


    /**
     * 删除用户（TODO：异常需要捕获）
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping(path = "deleteUser")
    public String deleteUser(Long userId) throws Exception {
        userService.deleteUser(userId);
        return "";
    }

    /**
     * 保存用户详情
     *
     * @param req
     * @return
     */
    @PostMapping(path = "saveUserInfo")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> saveUserInfo(UserInfoSaveReq req) {
        userService.saveUserInfo(req);
        return Result.ok(1);
    }

    /**
     * 删除用户详情（TODO：异常需要捕获）
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping(path = "deleteUserInfo")
    public String deleteUserInfo(Long userId) throws Exception {
        userService.deleteUserInfo(userId);
        return "";
    }

    /**
     * 获取用户主页信息
     *
     * @return
     */
    @GetMapping(path = "home")
    public String getUserHome(Model model, HttpServletRequest request) {

        String homeSelectType = request.getParameter("homeSelectType");
        if (homeSelectType == null || homeSelectType.equals(Strings.EMPTY)) {
            homeSelectType = HomeSelectEnum.ARTICLE.getCode();
        }

        String followSelectType = request.getParameter("followSelectType");
        if (followSelectType == null || followSelectType.equals(Strings.EMPTY)) {
            followSelectType = FollowTypeEnum.FOLLOW.getCode();
        }

        Long userId = ReqInfoContext.getReqInfo().getUserId();
        UserHomeDTO userHomeDTO = userService.getUserHomeDTO(userId);
        List<TagSelectDTO> homeSelectTags = homeSelectTags(homeSelectType);
        userHomeSelectList(homeSelectType, followSelectType, userId, model);

        model.addAttribute("homeSelectType", homeSelectType);
        model.addAttribute("homeSelectTags", homeSelectTags);
        model.addAttribute("userHome", userHomeDTO);
        return "biz/user/home";
    }

    /**
     * 返回Home页选择列表标签
     *
     * @param selectType
     * @return
     */
    private List<TagSelectDTO> homeSelectTags(String selectType) {
        List<TagSelectDTO> tagSelectDTOS = new ArrayList<>();
        homeSelectTags.forEach(tag -> {
            TagSelectDTO tagSelectDTO = new TagSelectDTO();
            tagSelectDTO.setSelectType(tag);
            tagSelectDTO.setSelectDesc(HomeSelectEnum.formCode(tag).getDesc());
            tagSelectDTO.setSelected(selectType.equals(tag) ? Boolean.TRUE : Boolean.FALSE);
            tagSelectDTOS.add(tagSelectDTO);
        });
        return tagSelectDTOS;
    }

    /**
     * 返回关注用户选择列表标签
     *
     * @param selectType
     * @return
     */
    private List<TagSelectDTO> followSelectTags(String selectType) {
        List<TagSelectDTO> tagSelectDTOS = new ArrayList<>();
        followSelectTags.forEach(tag -> {
            TagSelectDTO tagSelectDTO = new TagSelectDTO();
            tagSelectDTO.setSelectType(tag);
            tagSelectDTO.setSelectDesc(FollowSelectEnum.formCode(tag).getDesc());
            tagSelectDTO.setSelected(selectType.equals(tag) ? Boolean.TRUE : Boolean.FALSE);
            tagSelectDTOS.add(tagSelectDTO);
        });
        return tagSelectDTOS;
    }

    /**
     * 返回选择列表
     *
     * @param homeSelectType
     * @param userId
     * @param model
     */
    private void userHomeSelectList(String homeSelectType, String followSelectType, Long userId,  Model model) {
        PageParam pageParam = PageParam.newPageInstance(1L, 10L);
        if (homeSelectType.equals(HomeSelectEnum.ARTICLE.getCode())) {
            ArticleListDTO articleListDTO = articleService.getArticleListByUserId(userId, pageParam);
            model.addAttribute("homeSelectList", articleListDTO);
        } else if (homeSelectType.equals(HomeSelectEnum.READ.getCode())) {
            ArticleListDTO articleListDTO = articleService.getReadArticleListByUserId(userId, pageParam);
            model.addAttribute("homeSelectList", articleListDTO);
        }  else if (homeSelectType.equals(HomeSelectEnum.COLLECTION.getCode())) {
            ArticleListDTO articleListDTO = articleService.getCollectionArticleListByUserId(userId, pageParam);
            model.addAttribute("homeSelectList", articleListDTO);
        } else if (homeSelectType.equals(HomeSelectEnum.FOLLOW.getCode())) {

            // 关注用户与被关注用户
            // 获取选择标签
            List<TagSelectDTO> followSelectTags = followSelectTags(followSelectType);

            if (followSelectType.equals(FollowTypeEnum.FOLLOW.getCode())) {
                UserFollowListDTO userFollowListDTO = userRelationService.getUserFollowList(userId, pageParam);
                model.addAttribute("followList", userFollowListDTO);
            } else {
                UserFollowListDTO userFollowListDTO = userRelationService.getUserFansList(userId, pageParam);
                model.addAttribute("fansList", userFollowListDTO);
            }
            model.addAttribute("followSelectType", followSelectType);
            model.addAttribute("followSelectTags", followSelectTags);
        }
    }

    /**
     * 保存用户关系
     *
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(path = "saveUserRelation")
    public String saveUserRelation(UserRelationReq req) throws Exception {
        userRelationService.saveUserRelation(req);
        return "";
    }
}
