package cor.chrissy.community.web.backstage.view;

import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.statistics.dto.StatisticsCountDTO;
import cor.chrissy.community.service.statistics.dto.StatisticsDayDTO;
import cor.chrissy.community.service.statistics.service.StatisticsSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "backstage/statistics/")
public class StatisticsSettingViewController {

    @Autowired
    private StatisticsSettingService statisticsSettingService;

    static final Integer DEFAULT_DAY = 7;

    @ResponseBody
    @GetMapping(path = "queryTotal")
    public Result<StatisticsCountDTO> queryTotal() {
        StatisticsCountDTO statisticsCountDTO = statisticsSettingService.getStatisticsCount();
        return Result.ok(statisticsCountDTO);
    }

    @ResponseBody
    @GetMapping(path = "pvDayList")
    public Result<List<StatisticsDayDTO>> pvDayList(@RequestParam(name = "day", required = false) Integer day) {
        day = (day == null || day == 0) ? DEFAULT_DAY : day;
        List<StatisticsDayDTO> pvDayList = statisticsSettingService.getPvDayList(day);
        return Result.ok(pvDayList);
    }

    @ResponseBody
    @GetMapping(path = "uvDayList")
    public Result<List<StatisticsDayDTO>> uvDayList(@RequestParam(name = "day", required = false) Integer day) {
        day = (day == null || day == 0) ? DEFAULT_DAY : day;
        List<StatisticsDayDTO> pvDayList = statisticsSettingService.getUvDayList(day);
        return Result.ok(pvDayList);
    }
}
