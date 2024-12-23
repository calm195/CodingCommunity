package cor.chrissy.community.web.global.vo;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.web.config.GlobalViewConfig;
import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Data
public class GlobalVo {
    /**
     * 网站相关配置
     */
    private GlobalViewConfig siteInfo;
    /**
     * 环境
     */
    private String env;

    /**
     * 是否已登录
     */
    private Boolean isLogin;

    /**
     * 登录用户信息
     */
    private BaseUserInfoDTO user;

    /**
     * 消息通知数量
     */
    private Integer msgNum;


    private String currentDomain;
}
