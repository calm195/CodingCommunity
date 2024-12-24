package cor.chrissy.community.common.exception;

import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.result.Status;
import lombok.Getter;

/**
 * 该社区系统统一异常类
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Getter
public class CommunityException extends RuntimeException {
    private final Status status;

    public CommunityException(Status status) {
        this.status = status;
    }

    public CommunityException(int code, String msg) {
        this.status = Status.newStatus(code, msg);
    }

    public CommunityException(StatusEnum statusEnum, Object... args) {
        this.status = Status.newStatus(statusEnum, args);
    }
}
