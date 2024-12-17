package cor.chrissy.community.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * DTO 公共属性
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Data
public class BaseDTO {

    private Long id;

    private Date createTime;

    private Date updateTime;
}
