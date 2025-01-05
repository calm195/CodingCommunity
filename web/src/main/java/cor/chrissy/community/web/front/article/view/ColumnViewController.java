package cor.chrissy.community.web.front.article.view;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.ColumnTypeEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.core.util.ExceptionUtil;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.ColumnArticlesDTO;
import cor.chrissy.community.service.article.dto.ColumnDTO;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.article.service.ColumnService;
import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import cor.chrissy.community.service.comment.service.CommentReadService;
import cor.chrissy.community.service.sidebar.dto.SideBarDTO;
import cor.chrissy.community.service.sidebar.service.SidebarService;
import cor.chrissy.community.web.front.article.vo.ColumnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Controller
@RequestMapping(path = "column")
public class ColumnViewController {
    @Autowired
    private ColumnService columnService;
    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private SidebarService sidebarService;

    /**
     * 专栏列表
     *
     * @param model
     * @return
     */
    @GetMapping(path = {"list", "/", "", "home"})
    public String list(Model model) {
        PageListVo<ColumnDTO> columns = columnService.listColumn(PageParam.newPageInstance());
        List<SideBarDTO> sidebars = sidebarService.queryColumnSidebarList();
        ColumnVo vo = new ColumnVo();
        vo.setColumns(columns);
        vo.setSideBarItems(sidebars);
        model.addAttribute("vo", vo);
        return "views/column-home/index";
    }


    /**
     * 专栏详情
     *
     * @param columnId
     * @return
     */
    @GetMapping(path = "{columnId}")
    public String column(@PathVariable("columnId") Long columnId, Model model) {
        ColumnDTO dto = columnService.queryColumnInfo(columnId);
        if (dto == null) {
            throw ExceptionUtil.of(StatusEnum.COLUMN_NOT_EXISTS, "专栏不存在");
        }
        model.addAttribute("vo", dto);
        return "/views/column-index/index"; // TODO 未完成
    }


    /**
     * 专栏的文章阅读界面
     *
     * @param columnId 专栏id
     * @param section  节数，从1开始
     * @param model
     * @return
     */
    @GetMapping(path = "{columnId}/{section}")
    public String articles(@PathVariable("columnId") Long columnId, @PathVariable("section") Integer section, Model model) {
        if (section <= 0) section = 1;

        ColumnDTO columnDTO = columnService.queryBaseColumnInfo(columnId);

        Long articleId = columnService.queryColumnArticle(columnId, section);
        // 文章信息
        ArticleDTO articleDTO = articleReadService.queryTotalArticleInfo(articleId, ReqInfoContext.getReqInfo().getUserId());

        // 评论信息
        List<TopCommentDTO> comments = commentReadService.getArticleComments(articleId, PageParam.newPageInstance());

        // 热门评论
        TopCommentDTO hotComment = commentReadService.queryHotComment(articleId);

        // 文章列表
        List<SimpleArticleDTO> articles = columnService.queryColumnArticles(columnId);

        ColumnArticlesDTO vo = new ColumnArticlesDTO();
        updateReadType(vo, columnDTO, articleDTO);
        vo.setArticle(articleDTO);
        vo.setComments(comments);
        vo.setHotComment(hotComment);
        vo.setColumn(columnId);
        vo.setArticleList(articles);
        vo.setSection(section);
        model.addAttribute("vo", vo);
        return "/views/column-detail/index";
    }

    private void updateReadType(ColumnArticlesDTO vo, ColumnDTO columnDTO, ArticleDTO articleDTO) {
        Long loginUserId = ReqInfoContext.getReqInfo().getUserId();
        if (loginUserId != null && loginUserId.equals(articleDTO.getAuthor())) {
            vo.setReadType(0);
            return;
        }

        if (columnDTO.getType() == ColumnTypeEnum.TIME_FREE.getType()) {
            long now = System.currentTimeMillis();
            if (now > columnDTO.getFreeEndTime() || now < columnDTO.getFreeStartTime()) {
                vo.setReadType(ColumnTypeEnum.LOGIN.getType());
            } else {
                vo.setReadType(ColumnTypeEnum.FREE.getType());
            }
        } else {
            vo.setReadType(columnDTO.getType());
        }

        if (vo.getReadType() == ColumnTypeEnum.LOGIN.getType()) {
            String content = articleDTO.getContent();
            if (content.length() > 300) {
                content = content.substring(0, 300);
            }
            articleDTO.setContent(content);
        }
    }
}
