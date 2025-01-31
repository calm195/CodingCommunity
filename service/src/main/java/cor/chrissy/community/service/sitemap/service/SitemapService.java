package cor.chrissy.community.service.sitemap.service;

import cor.chrissy.community.service.sitemap.model.SiteMap;

/**
 * @author wx128
 * @createAt 2025/1/14
 */
public interface SitemapService {

    /**
     * 查询站点地图
     *
     * @return
     */
    SiteMap getSiteMap();

    /**
     * 刷新站点地图
     */
    void refreshSitemap();

    /**
     * 新增文章并上线
     *
     * @param articleId
     */
    void addArticle(Long articleId);

    /**
     * 删除文章、or文章下线
     *
     * @param articleId
     */
    void rmArticle(Long articleId);
}