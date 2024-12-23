package cor.chrissy.community.service.article.dto;

import cor.chrissy.community.service.user.dto.SimpleUserInfoDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文章传输DTO
 *
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
public class ArticleDTO implements Serializable {
    private static final long serialVersionUID = -793906904770296838L;

    private Long articleId;

    /**
     * 文章类型：1-博文，2-问答
     */
    private Integer articleType;

    /**
     * 作者uid
     */
    private Long author;

    private String authorName;

    private String authorAvatar;

    private String title;

    private String shortTitle;
    /**
     * 简介
     */
    private String summary;

    /**
     * 封面
     */
    private String cover;

    /**
     * 正文
     */
    private String content;

    /**
     * 文章来源
     *
     * @see cor.chrissy.community.common.enums.SourceTypeEnum
     */
    private String sourceType;

    /**
     * 原文地址
     */
    private String sourceUrl;

    /**
     * 0 未发布 1 已发布
     */
    private Integer status;

    private Integer flagBit;

    private Boolean isOfficial;

    private Boolean isTopping;

    private Boolean isCream;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 最后更新时间
     */
    private Long lastUpdateTime;

    /**
     * 分类
     */
    private CategoryDTO category;

    /**
     * 标签
     */
    private List<TagDTO> tags;

    /**
     * 计数统计相关
     */
    private ArticleFootCountDTO articleFootCount;

    /**
     * 表示当前查看的用户是否已经点赞过
     */
    private Boolean praised;

    /**
     * 表示当用户是否评论过
     */
    private Boolean commented;

    /**
     * 表示当前用户是否收藏过
     */
    private Boolean collected;

    /**
     * 点赞用户信息
     */
    private List<SimpleUserInfoDTO> praisedUsers;
}
