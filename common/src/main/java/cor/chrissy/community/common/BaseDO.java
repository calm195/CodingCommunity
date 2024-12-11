package cor.chrissy.community.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Data
public class BaseDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Date createTime;

    private Date updateTime;
}
