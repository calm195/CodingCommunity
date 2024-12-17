package cor.chrissy.community.core.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具类
 *
 * @author wx128
 * @createAt 2024/12/13
 */
public class DateUtil {
    /**
     * 时间戳转日常日期格式  yyyy年MM月dd日 HH:mm
     *
     * @param timestamp
     * @return
     */
    public static String time2day(long timestamp) {
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
    }
}
