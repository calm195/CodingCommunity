package cor.chrissy.community.service.config.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import cor.chrissy.community.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("dict_common")
public class DictCommonDO extends BaseDO {
    /**
     * 字典类型
     */
    @TableField("type_code")
    private String typeCode;

    /**
     * 字典类型的值编码
     */
    @TableField("dict_code")
    private String dictCode;

    /**
     * 字典类型的值描述
     */
    @TableField("dict_desc")
    private String dictDesc;

    /**
     * 排序编号
     */
    @TableField("sort_no")
    private Integer sortNo;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}

