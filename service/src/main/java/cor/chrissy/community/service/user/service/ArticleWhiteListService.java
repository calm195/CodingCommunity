package cor.chrissy.community.service.user.service;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2025/1/14
 */
public interface ArticleWhiteListService {

    /**
     * 判断作者是否再文章发布的白名单中；
     * 这个白名单主要是用于控制作者发文章之后是否需要进行审核
     *
     * @param authorId
     * @return
     */
    boolean authorInArticleWhiteList(Long authorId);

    /**
     * 获取所有的白名单用户
     *
     * @return
     */
    List<BaseUserInfoDTO> queryAllArticleWhiteListAuthors();

    /**
     * 将用户添加到白名单中
     *
     * @param userId
     */
    void addAuthor2ArticleWhitList(Long userId);

    /**
     * 从白名单中移除用户
     *
     * @param userId
     */
    void removeAuthorFromArticelWhiteList(Long userId);

}