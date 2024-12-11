package cor.chrissy.community.web.front.user;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserRelationReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.service.article.dto.ArticleListDTO;
import cor.chrissy.community.service.article.service.ArticleService;
import cor.chrissy.community.service.user.dto.UserFollowListDTO;
import cor.chrissy.community.service.user.dto.UserHomeDTO;
import cor.chrissy.community.service.user.service.UserRelationService;
import cor.chrissy.community.service.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Controller
@RequestMapping(path = "user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private UserRelationService userRelationService;

    @Resource
    private ArticleService articleService;

    /**
     * 保存用户（TODO：异常需要捕获）
     *
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(path = "saveUser")
    public String saveUser(UserSaveReq req) throws Exception {
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
     * 保存用户详情（TODO：异常需要捕获）
     *
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(path = "saveUserInfo")
    public String saveUserInfo(UserInfoSaveReq req) throws Exception {
        userService.saveUserInfo(req);
        return "";
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
     * 获取用户主页信息（TODO：异常需要捕获）
     *
     * @param userId
     * @return
     */
    @GetMapping(path = "home")
    public String getUserHomeDTO(Long userId, Model model) throws Exception {
        UserHomeDTO userHomeDTO = userService.getUserHomeDTO(userId);
        model.addAttribute("userHome", userHomeDTO);
        return "biz/user/home";
    }

    /**
     * 获取我关注的用户列表（TODO：异常需要捕获）
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(path = "followList")
    public String followList(Long userId, Long pageNum, Long pageSize, Model model) throws Exception {
        pageNum = (pageNum == null) ? 1L : pageNum;
        pageSize = (pageSize == null) ? 10L : pageSize;
        UserFollowListDTO userFollowListDTO = userRelationService.getUserFollowList(userId, PageParam.newPageInstance(pageNum, pageSize));
        model.addAttribute("followList", userFollowListDTO);
        return "biz/user/followList";
    }


    /**
     * 获取我关注的用户列表（TODO：异常需要捕获）
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(path = "fansList")
    public String fansList(Long userId, Long pageNum, Long pageSize, Model model) throws Exception {
        pageNum = (pageNum == null) ? 1L : pageNum;
        pageSize = (pageSize == null) ? 10L : pageSize;
        UserFollowListDTO userFollowListDTO = userRelationService.getUserFansList(userId, PageParam.newPageInstance(pageNum, pageSize));
        model.addAttribute("fansList", userFollowListDTO);
        return "biz/user/fansList";
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

    /**
     * 获取用户文章列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping(path = "articleList")
    public String getArticleList(Long userId, Long pageNum, Long pageSize, Model model) {
        pageNum = (pageNum == null) ? 1L : pageNum;
        pageSize = (pageSize == null) ? 10L : pageSize;

        ArticleListDTO articleListDTO = articleService.getArticleListByUserId(userId, PageParam.newPageInstance(pageNum, pageSize));
        model.addAttribute("articleList", articleListDTO);
        return "biz/user/articleList";
    }

    /**
     * 获取用户收藏的文章列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping(path = " collectionArticleList")
    public String getCollectionArticleList(Long userId, Long pageNum, Long pageSize, Model model) {
        pageNum = (pageNum == null) ? 1L : pageNum;
        pageSize = (pageSize == null) ? 10L : pageSize;

        ArticleListDTO articleListDTO = articleService.getCollectionArticleListByUserId(userId, PageParam.newPageInstance(pageNum, pageSize));
        model.addAttribute("collectionArticleList", articleListDTO);
        return "biz/user/collectionArticleList";
    }

    /**
     * 获取用户阅读的文章列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping(path = "readArticleList")
    public String getReadArticleList(Long userId, Long pageNum, Long pageSize, Model model) {
        pageNum = (pageNum == null) ? 1L : pageNum;
        pageSize = (pageSize == null) ? 10L : pageSize;

        ArticleListDTO articleListDTO = articleService.getReadArticleListByUserId(userId, PageParam.newPageInstance(pageNum, pageSize));
        model.addAttribute("readArticleList", articleListDTO);
        return "biz/user/readArticleList";
    }
}
