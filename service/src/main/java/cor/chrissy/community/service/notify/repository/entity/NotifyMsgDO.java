package cor.chrissy.community.service.notify.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import cor.chrissy.community.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Data
@Accessors(chain = true)
@TableName("notify_msg")
@EqualsAndHashCode(callSuper = true)
public class NotifyMsgDO extends BaseDO {
    private static final long serialVersionUID = -4043774744889659100L;

    /**
     * 消息关联的主体
     * - 如文章收藏、评论、回复评论、点赞消息，这里存文章ID；
     * - 如系统通知消息时，这里存的是系统通知消息正文主键，也可以是0
     * - 如关注，这里就是0
     */
    private Long relatedId;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 消息通知的用户id
     */
    private Long notifyUserId;

    /**
     * 触发这个消息的用户id
     */
    private Long operateUserId;

    /**
     * 消息类型
     *
     * @see cor.chrissy.community.common.enums.NotifyTypeEnum#getType()
     */
    private Integer type;

    /**
     * 0 未查看 1 已查看
     */
    private Integer state;
}
