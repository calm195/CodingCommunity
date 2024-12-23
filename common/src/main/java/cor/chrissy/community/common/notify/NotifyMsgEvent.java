package cor.chrissy.community.common.notify;

import cor.chrissy.community.common.enums.NotifyTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class NotifyMsgEvent<T> extends ApplicationEvent {

    private NotifyTypeEnum notifyType;

    private T content;

    public NotifyMsgEvent(Object source, NotifyTypeEnum notifyType, T content) {
        super(source);
        this.notifyType = notifyType;
        this.content = content;
    }
}

