package cor.chrissy.community.service.sitemap.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import cor.chrissy.community.common.enums.ArticleEventEnum;
import cor.chrissy.community.common.req.article.ArticleMsgEvent;
import cor.chrissy.community.core.cache.RedisClient;
import cor.chrissy.community.core.util.DateUtil;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.repository.dao.ArticleDao;
import cor.chrissy.community.service.sitemap.model.SiteMap;
import cor.chrissy.community.service.sitemap.model.SiteUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wx128
 * @createAt 2025/1/14
 */
@Slf4j
@Service
public class SitemapServiceImpl implements SitemapService {
    @Value("${view.site.host:https://paicoding.com}")
    private String host;
    private static final int SCAN_SIZE = 100;

    private static final String SITE_MAP_CACHE_KEY = "sitemap";

    @Resource
    private ArticleDao articleDao;

    public SiteMap getSiteMap() {
        // key = 文章id, value = 最后更新时间
        Map<String, Long> siteMap = RedisClient.hGetAll(SITE_MAP_CACHE_KEY, Long.class);
        if (CollectionUtils.isEmpty(siteMap)) {
            // 首次访问时，没有数据，全量初始化
            initSiteMap();
        }
        siteMap = RedisClient.hGetAll(SITE_MAP_CACHE_KEY, Long.class);
        SiteMap vo = initBasicSite();
        if (CollectionUtils.isEmpty(siteMap)) {
            return vo;
        }

        for (Map.Entry<String, Long> entry : siteMap.entrySet()) {
            vo.addUrl(new SiteUrl(host + "/article/detail/" + entry.getKey(), DateUtil.time2utc(entry.getValue())));
        }
        return vo;
    }

    /**
     * 重新刷新站点地图
     */
    public void refreshSitemap() {
        initSiteMap();
    }

    public void addArticle(Long articleId) {
        RedisClient.hSet(SITE_MAP_CACHE_KEY, String.valueOf(articleId), System.currentTimeMillis());
    }

    public void rmArticle(Long articleId) {
        RedisClient.hDel(SITE_MAP_CACHE_KEY, String.valueOf(articleId));
    }

    /**
     * fixme: 加锁初始化，更推荐的是采用分布式锁
     */
    private synchronized void initSiteMap() {
        long lastId = 0L;
        RedisClient.del(SITE_MAP_CACHE_KEY);
        while (true) {
            List<SimpleArticleDTO> list = articleDao.getBaseMapper().listArticlesOrderById(lastId, SCAN_SIZE);
            RedisClient.hMSet(SITE_MAP_CACHE_KEY,
                    list.stream().collect(Collectors.toMap(s -> String.valueOf(s.getId()),
                            s -> s.getCreateTime().getTime(),
                            (a, b) -> a)));
            if (list.size() < SCAN_SIZE) {
                break;
            }
            lastId = list.get(list.size() - 1).getId();
        }
    }

    private SiteMap initBasicSite() {
        SiteMap vo = new SiteMap();
        String time = DateUtil.time2utc(System.currentTimeMillis());
        vo.addUrl(new SiteUrl(host + "/", time));
        vo.addUrl(new SiteUrl(host + "/column", time));
        vo.addUrl(new SiteUrl(host + "/admin-view", time));
        return vo;
    }

    /**
     * 基于文章的上下线，自动更新站点地图
     *
     * @param event
     */
    @EventListener(ArticleMsgEvent.class)
    public void autoUpdateSiteMap(ArticleMsgEvent<Long> event) {
        ArticleEventEnum type = event.getType();
        if (type == ArticleEventEnum.ONLINE) {
            addArticle(event.getContent());
        } else if (type == ArticleEventEnum.OFFLINE || type == ArticleEventEnum.DELETE) {
            rmArticle(event.getContent());
        }
    }

    /**
     * 采用定时器方案，每天5:15分刷新站点地图，确保数据的一致性
     */
    @Scheduled(cron = "0 15 5 * * ?")
    public void autoRefreshCache() {
        log.info("开始刷新sitemap.xml的url地址，避免出现数据不一致问题!");
        refreshSitemap();
        log.info("刷新完成！");
    }
}

