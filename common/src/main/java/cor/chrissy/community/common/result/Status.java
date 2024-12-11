package cor.chrissy.community.common.result;

import cor.chrissy.community.common.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态值类，用于后端返回参数
 *
 * @author wx128
 * @createAt 2024/12/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    private int code;
    private String message;

    public static Status newStatus(int code, String message) {
        return new Status(code, message);
    }

    public static Status newStatus(StatusEnum status, Object... msgs) {
        String msg;
        if (msgs.length > 0) {
            msg = String.format(status.getMsg(), msgs);
        } else {
            msg = status.getMsg();
        }
        return newStatus(status.getCode(), msg);
    }
}
