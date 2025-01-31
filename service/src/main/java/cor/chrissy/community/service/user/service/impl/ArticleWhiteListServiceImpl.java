package cor.chrissy.community.service.user.service.impl;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.core.cache.RedisClient;
import cor.chrissy.community.service.user.service.ArticleWhiteListService;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author wx128
 * @createAt 2025/1/14
 */
@Service
public class ArticleWhiteListServiceImpl implements ArticleWhiteListService {
    /**
     * 实用 redis - set 来存储允许直接发文章的白名单
     */
    private static final String ARTICLE_WHITE_LIST = "auth_article_white_list";

    @Autowired
    private UserService userService;

    @Override
    public boolean authorInArticleWhiteList(Long authorId) {
        return RedisClient.sIsMember(ARTICLE_WHITE_LIST, authorId);
    }

    /**
     * 获取所有的白名单用户
     *
     * @return
     */
    public List<BaseUserInfoDTO> queryAllArticleWhiteListAuthors() {
        Set<Long> users = RedisClient.sGetAll(ARTICLE_WHITE_LIST, Long.class);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return userService.batchQueryBasicUserInfo(users);
    }

    public void addAuthor2ArticleWhitList(Long userId) {
        RedisClient.sPut(ARTICLE_WHITE_LIST, userId);
    }

    public void removeAuthorFromArticelWhiteList(Long userId) {
        RedisClient.sDel(ARTICLE_WHITE_LIST, userId);
    }
}

