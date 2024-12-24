package cor.chrissy.community.web.account.vo;

import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Data
public class QrLoginVo {
    /**
     * 验证码
     */
    private String code;

    /**
     * 二维码
     */
    private String qr;

    /**
     * true 表示需要重新建立连接
     */
    private boolean reconnect;
}
