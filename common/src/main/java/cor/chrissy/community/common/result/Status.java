package cor.chrissy.community.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    private int code;
    private String message;

    public static Status newState(int code, String message) {
        return new Status(code, message);
    }
}
