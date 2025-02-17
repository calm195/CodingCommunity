package cor.chrissy.community.common.req.recommend;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 图片与跳转信息
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
@Accessors(chain = true)
public class CarouseDTO implements Serializable {

    private static final long serialVersionUID = 1048555496974144842L;
    /**
     * 说明
     */
    private String name;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 跳转地址
     */
    private String actionUrl;
}
