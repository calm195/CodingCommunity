package cor.chrissy.community.common.result;

import cor.chrissy.community.common.enums.StatusEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端返回包装体
 *
 * @author wx128
 * @createAt 2024/12/9
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -510306209659393854L;

    private Status status;
    private T result;

    public Result(Status status) {
        this.status = status;
    }

    public Result(T t) {
        status = Status.newStatus(StatusEnum.SUCCESS);
        this.result = t;
    }

    public static <T> Result<T> ok(T t) {
        return new Result<>(t);
    }

    public static <T> Result<T> fail(StatusEnum status, Object... messages) {
        return new Result<>(Status.newStatus(status, messages));
    }

    public static <T> Result<T> fail(Status status) {
        return new Result<>(status);
    }
}
