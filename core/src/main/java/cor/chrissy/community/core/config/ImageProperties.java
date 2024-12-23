package cor.chrissy.community.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 图片配置
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "image")
public class ImageProperties {

    /**
     * 存储绝对路径
     */
    private String absTmpPath;

    /**
     * 存储相对路径
     */
    private String webImgPath;

    /**
     * 上传文件的临时存储目录
     */
    private String tmpUploadPath;
}

