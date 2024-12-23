package cor.chrissy.community.service.statistics.service;

import cor.chrissy.community.service.statistics.dto.StatisticsCountDTO;
import cor.chrissy.community.service.statistics.dto.StatisticsDayDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface StatisticsSettingService {
    /**
     * 保存计数
     *
     * @param host
     */
    void saveRequestCount(String host);

    /**
     * 获取总数
     *
     * @return
     */
    StatisticsCountDTO getStatisticsCount();

    /**
     * 获取每天的PV统计数据
     *
     * @param day
     * @return
     */
    List<StatisticsDayDTO> getPvDayList(Integer day);

    /**
     * 获取每天的UV统计数据
     *
     * @param day
     * @return
     */
    List<StatisticsDayDTO> getUvDayList(Integer day);
}
