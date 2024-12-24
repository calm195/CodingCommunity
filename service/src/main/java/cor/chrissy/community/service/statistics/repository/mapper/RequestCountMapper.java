package cor.chrissy.community.service.statistics.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cor.chrissy.community.service.statistics.dto.StatisticsDayDTO;
import cor.chrissy.community.service.statistics.repository.entity.RequestCountDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface RequestCountMapper extends BaseMapper<RequestCountDO> {

    /**
     * 获取 PV 总数
     *
     * @return
     */
    Long getPvTotalCount();

    /**
     * 获取 PV 数据列表
     *
     * @param day
     * @return
     */
    List<StatisticsDayDTO> getPvDayList(@Param("day") Integer day);

    /**
     * 获取 UV 数据列表
     *
     * @param day
     * @return
     */
    List<StatisticsDayDTO> getUvDayList(@Param("day") Integer day);
}

