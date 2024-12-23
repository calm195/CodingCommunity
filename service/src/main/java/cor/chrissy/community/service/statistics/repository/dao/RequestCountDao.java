package cor.chrissy.community.service.statistics.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cor.chrissy.community.service.statistics.dto.StatisticsDayDTO;
import cor.chrissy.community.service.statistics.repository.entity.RequestCountDO;
import cor.chrissy.community.service.statistics.repository.mapper.RequestCountMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Repository
public class RequestCountDao extends ServiceImpl<RequestCountMapper, RequestCountDO> {

    /**
     * 获取请求数据
     *
     * @param host
     * @param date
     * @return
     */
    public RequestCountDO getRequestCount(String host, Date date) {
        return lambdaQuery()
                .eq(RequestCountDO::getHost, host)
                .eq(RequestCountDO::getDate, date)
                .one();
    }

    /**
     * 获取 PV 总数
     *
     * @return
     */
    public Integer getPvTotalCount() {
        return baseMapper.getPvTotalCount().intValue();
    }

    /**
     * 获取 PV 数据列表
     * @param day
     * @return
     */
    public List<StatisticsDayDTO> getPvDayList(Integer day) {
        return baseMapper.getPvDayList(day);
    }

    /**
     * 获取 UV 数据列表
     *
     * @param day
     * @return
     */
    public List<StatisticsDayDTO> getUvDayList(Integer day) {
        return baseMapper.getUvDayList(day);
    }
}

