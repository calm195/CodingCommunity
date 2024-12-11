package cor.chrissy.community.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取网站配置信息。从配置文件application-config.yml中读取配置信息
 *
 * @author wx128
 * @createAt 2024/12/9
 */
@Data
@Component
@ConfigurationProperties(prefix = "view.site")
public class GlobalViewConfig {
    private String name;
    private String logoUrl;
    private String faviconIconUrl;

    private String contactMeTitle;
    private String contactMeEmail;

    private Integer pageSize;
    private String cdnImgStyle;
    private String websiteRecord;
}
