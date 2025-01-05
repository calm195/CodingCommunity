package cor.chrissy.community.service.user.dto;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.service.article.dto.YearArticleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 用户主页详情DTO
 *
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserStatisticInfoDTO extends BaseUserInfoDTO {

    /**
     * 关注数
     */
    private Integer followCount;

    /**
     * 粉丝数
     */
    private Integer fansCount;

    /**
     * 已发布文章数
     */
    private Integer articleCount;

    /**
     * 文章点赞数
     */
    private Integer praiseCount;

    /**
     * 文章被阅读数
     */
    private Integer readCount;

    /**
     * 文章被收藏数
     */
    private Integer collectionCount;

    private Integer joinDayCount;

    private Boolean followed;

    private List<YearArticleDTO> yearArticleList;

    private Integer infoPercent;
}
