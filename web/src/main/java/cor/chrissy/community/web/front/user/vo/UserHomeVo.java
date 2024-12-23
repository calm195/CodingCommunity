package cor.chrissy.community.web.front.user.vo;

import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.TagSelectDTO;
import cor.chrissy.community.service.user.dto.UserFollowListDTO;
import cor.chrissy.community.service.user.dto.UserStatisticInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Data
public class UserHomeVo {
    private String homeSelectType;
    private List<TagSelectDTO> homeSelectTags;
    private UserFollowListDTO fansList;
    private UserFollowListDTO followList;
    private String followSelectType;
    private List<TagSelectDTO> followSelectTags;
    private UserStatisticInfoDTO userHome;
    private PageListVo<ArticleDTO> homeSelectList;
}
