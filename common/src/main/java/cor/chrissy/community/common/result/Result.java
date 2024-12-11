package cor.chrissy.community.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -510306209659393854L;

    private Status status;
    private T result;

    public Result(int code, String message){
        this.status = new Status(code, message);
    }

    public Result(T t){
        this.status = Status.newState(0, "ok");
        this.result = t;
    }

    public static <T> Result<T> success(T t){
        return new Result<T>(t);
    }

    public static <T> Result<T> error(Status status, String... messages){
        String message = "";
        if (messages != null && messages.length > 0){
            message = String.format(status.getMessage(), (Object) messages);
        } else {
            message = status.getMessage();
        }

        return new Result<>(status.getCode(), message);
    }
}
