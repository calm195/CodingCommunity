package cor.chrissy.community.web.front.article.vo;

import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import cor.chrissy.community.service.sidebar.dto.SideBarDTO;
import cor.chrissy.community.service.user.dto.UserStatisticInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Data
public class ArticleDetailVo {
    /**
     * 文章信息
     */
    private ArticleDTO article;

    /**
     * 评论信息
     */
    private List<TopCommentDTO> comments;

    /**
     * 热门评论
     */
    private TopCommentDTO hotComment;

    /**
     * 作者相关信息
     */
    private UserStatisticInfoDTO author;

    /**
     * 侧边栏信息
     */
    private List<SideBarDTO> sideBarItems;

}

