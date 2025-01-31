package cor.chrissy.community.web.front.user.view;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.FollowSelectEnum;
import cor.chrissy.community.common.enums.FollowTypeEnum;
import cor.chrissy.community.common.enums.HomeSelectEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.core.util.SpringUtil;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.TagSelectDTO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.user.dto.FollowUserInfoDTO;
import cor.chrissy.community.service.user.dto.UserStatisticInfoDTO;
import cor.chrissy.community.service.user.service.UserRelationService;
import cor.chrissy.community.service.user.service.UserService;
import cor.chrissy.community.web.front.user.vo.UserHomeVo;
import cor.chrissy.community.web.global.BaseViewController;
import cor.chrissy.community.web.global.SeoInjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Controller
@RequestMapping(path = "user")
@Slf4j
public class UserViewController extends BaseViewController {

    @Resource
    private UserService userService;

    @Resource
    private UserRelationService userRelationService;

    @Resource
    private ArticleReadService articleReadService;

    private static final List<String> homeSelectTags = Arrays.asList("article", "read", "follow", "collection");
    private static final List<String> followSelectTags = Arrays.asList("follow", "fans");

    /**
     * 获取用户主页信息
     *
     * @return
     */
    @GetMapping(path = "home")
    @Permission(role = UserRole.LOGIN)
    public String getUserHome(@RequestParam(name = "userId") Long userId,
                              @RequestParam(name = "homeSelectType", required = false) String homeSelectType,
                              @RequestParam(name = "followSelectType", required = false) String followSelectType,
                              Model model) {
        UserHomeVo vo = new UserHomeVo();
        vo.setHomeSelectType(StringUtils.isBlank(homeSelectType) ? HomeSelectEnum.ARTICLE.getCode() : homeSelectType);
        vo.setFollowSelectType(StringUtils.isBlank(followSelectType) ? FollowTypeEnum.FOLLOW.getCode() : followSelectType);

        UserStatisticInfoDTO userInfo = userService.queryUserInfoWithStatistic(userId);
        vo.setUserHome(userInfo);

        List<TagSelectDTO> homeSelectTags = homeSelectTags(vo.getHomeSelectType(), Objects.equals(userId, ReqInfoContext.getReqInfo().getUserId()));
        vo.setHomeSelectTags(homeSelectTags);

        userHomeSelectList(vo, userId);
        model.addAttribute("vo", vo);
        SpringUtil.getBean(SeoInjectService.class).initUserSeo(vo);
        return "views/user/index";
    }

    @GetMapping(path = "/{userId}")
    public String detail(@PathVariable(name = "userId") Long userId, @RequestParam(name = "homeSelectType", required = false) String homeSelectType,
                         @RequestParam(name = "followSelectType", required = false) String followSelectType,
                         Model model) {
        UserHomeVo vo = new UserHomeVo();
        vo.setHomeSelectType(StringUtils.isBlank(homeSelectType) ? HomeSelectEnum.ARTICLE.getCode() : homeSelectType);
        vo.setFollowSelectType(StringUtils.isBlank(followSelectType) ? FollowTypeEnum.FOLLOW.getCode() : followSelectType);

        UserStatisticInfoDTO userInfo = userService.queryUserInfoWithStatistic(userId);
        vo.setUserHome(userInfo);

        List<TagSelectDTO> homeSelectTags = homeSelectTags(vo.getHomeSelectType(), Objects.equals(userId, ReqInfoContext.getReqInfo().getUserId()));
        vo.setHomeSelectTags(homeSelectTags);

        userHomeSelectList(vo, userId);
        model.addAttribute("vo", vo);
        SpringUtil.getBean(SeoInjectService.class).initUserSeo(vo);
        return "views/user/index";
    }

    /**
     * 返回Home页选择列表标签
     *
     * @param selectType
     * @return
     */
    private List<TagSelectDTO> homeSelectTags(String selectType, boolean isAuthor) {
        List<TagSelectDTO> tags = new ArrayList<>();
        homeSelectTags.forEach(tag -> {
            if (!isAuthor && tag.equalsIgnoreCase("read")) {
                return;
            }
            TagSelectDTO tagSelectDTO = new TagSelectDTO();
            tagSelectDTO.setSelectType(tag);
            tagSelectDTO.setSelectDesc(HomeSelectEnum.fromCode(tag).getDesc());
            tagSelectDTO.setSelected(selectType.equals(tag));
            tags.add(tagSelectDTO);
        });
        return tags;
    }

    /**
     * 返回关注用户选择列表标签
     *
     * @param selectType
     * @return
     */
    private List<TagSelectDTO> followSelectTags(String selectType) {
        List<TagSelectDTO> tags = new ArrayList<>();
        followSelectTags.forEach(tag -> {
            TagSelectDTO tagSelectDTO = new TagSelectDTO();
            tagSelectDTO.setSelectType(tag);
            tagSelectDTO.setSelectDesc(FollowSelectEnum.fromCode(tag).getDesc());
            tagSelectDTO.setSelected(selectType.equals(tag));
            tags.add(tagSelectDTO);
        });
        return tags;
    }

    /**
     * 返回选择列表
     *
     * @param vo
     * @param userId
     */
    private void userHomeSelectList(UserHomeVo vo, Long userId) {
        PageParam pageParam = PageParam.newPageInstance();
        HomeSelectEnum select = HomeSelectEnum.fromCode(vo.getHomeSelectType());
        if (select == null) {
            return;
        }

        switch (select) {
            case ARTICLE:
            case READ:
            case COLLECTION:
                PageListVo<ArticleDTO> dto = articleReadService.queryArticlesByUserAndType(userId, pageParam, select);
                vo.setHomeSelectList(dto);
                return;
            case FOLLOW:
                // 关注用户与被关注用户
                // 获取选择标签
                List<TagSelectDTO> followSelectTags = followSelectTags(vo.getFollowSelectType());
                vo.setFollowSelectTags(followSelectTags);
                initFollowFansList(vo, userId, pageParam);
                return;
            default:
        }
    }

    private void initFollowFansList(UserHomeVo vo, long userId, PageParam pageParam) {
        PageListVo<FollowUserInfoDTO> followList;
        boolean needUpdateRelation = false;
        if (vo.getFollowSelectType().equals(FollowTypeEnum.FOLLOW.getCode())) {
            followList = userRelationService.getUserFollowList(userId, pageParam);
        } else {
            followList = userRelationService.getUserFansList(userId, pageParam);
            needUpdateRelation = true;
        }

        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        if (!Objects.equals(loginUserId, userId) || needUpdateRelation) {
            userRelationService.updateUserFollowRelationId(followList, loginUserId);
        }
        vo.setFollowList(followList);
    }
}

