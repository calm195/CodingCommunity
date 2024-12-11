package cor.chrissy.community.service.user.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import cor.chrissy.community.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_relation")
public class UserRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 关注用户ID
     */
    private Long followUserId;

    private Integer deleted;
}
