package cor.chrissy.community.service.statistics.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import cor.chrissy.community.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("request_count")
public class RequestCountDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 机器IP
     */
    private String host;

    /**
     * 访问计数
     */
    private Integer cnt;

    /**
     * 当前日期
     */
    private Date date;
}
