package cor.chrissy.community.service.statistics.service.impl;

import cor.chrissy.community.service.article.service.ArticleSettingService;
import cor.chrissy.community.service.statistics.dto.StatisticsCountDTO;
import cor.chrissy.community.service.statistics.dto.StatisticsDayDTO;
import cor.chrissy.community.service.statistics.repository.dao.RequestCountDao;
import cor.chrissy.community.service.statistics.repository.entity.RequestCountDO;
import cor.chrissy.community.service.statistics.service.StatisticsSettingService;
import cor.chrissy.community.service.user.service.UserSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Slf4j
@Service
public class StatisticsSettingServiceImpl implements StatisticsSettingService {

    @Autowired
    private RequestCountDao requestCountDao;

    @Autowired
    private UserSettingService userSettingService;

    @Autowired
    private ArticleSettingService articleSettingService;

    @Override
    public void saveRequestCount(String host) {
        RequestCountDO requestCountDO = requestCountDao.getRequestCount(host, Date.valueOf(LocalDate.now()));
        if (requestCountDO == null) {
            try {
                requestCountDO = new RequestCountDO();
                requestCountDO.setHost(host);
                requestCountDO.setCnt(1);
                requestCountDO.setDate(Date.valueOf(LocalDate.now()));
                requestCountDao.save(requestCountDO);
            } catch (Exception e) {
                log.error("saveRequestCount error: {}", requestCountDO, e);
            }
        } else {
            // 计数无需精确值，不用考虑并发情况
            requestCountDO.setCnt(requestCountDO.getCnt() + 1);
            requestCountDao.updateById(requestCountDO);
        }
    }

    @Override
    public StatisticsCountDTO getStatisticsCount() {
        Integer userCount = userSettingService.getUserCount();
        Integer articleCount = articleSettingService.getArticleCount();
        Integer pvCount = requestCountDao.getPvTotalCount();

        StatisticsCountDTO totalCount = new StatisticsCountDTO();
        totalCount.setUserCount(userCount);
        totalCount.setArticleCount(articleCount);
        totalCount.setPvCount(pvCount);
        return totalCount;
    }

    @Override
    public List<StatisticsDayDTO> getPvDayList(Integer day) {
        return requestCountDao.getPvDayList(day);
    }

    @Override
    public List<StatisticsDayDTO> getUvDayList(Integer day) {
        return requestCountDao.getUvDayList(day);
    }
}
