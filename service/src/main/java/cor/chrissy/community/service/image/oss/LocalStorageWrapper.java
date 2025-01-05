package cor.chrissy.community.service.image.oss;

import com.github.hui.quick.plugin.base.FileWriteUtil;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.core.config.ImageProperties;
import cor.chrissy.community.core.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @author wx128
 * @createAt 2025/1/3
 */
@Slf4j
@Component
@ConditionalOnExpression(value = "#{'local'.equals(environment.getProperty('image.oss.type'))}")
public class LocalStorageWrapper implements IOssUploader {
    @Autowired
    private ImageProperties imageProperties;
    private Random random;

    public LocalStorageWrapper() {
        random = new Random();
    }

    @Override
    public String upload(InputStream input, String fileType) {
        try {
            if (fileType == null) {
                // 根据魔数判断文件类型
                byte[] bytes = StreamUtils.copyToByteArray(input);
                input = new ByteArrayInputStream(bytes);
                fileType = getFileType((ByteArrayInputStream) input, fileType);
            }

            String path = imageProperties.getAbsTmpPath() + imageProperties.getWebImgPath();
            String fileName = genTmpFileName();

            FileWriteUtil.FileInfo file = FileWriteUtil.saveFileByStream(input, path, fileName, fileType);
            return imageProperties.buildImgUrl(imageProperties.getWebImgPath() + file.getFilename() + "." + file.getFileType());
        } catch (Exception e) {
            log.error("Parse img from httpRequest to BufferedImage error! e:", e);
            throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED);
        }
    }

    /**
     * 获取文件临时名称
     *
     * @return
     */
    private String genTmpFileName() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS")) + "_" + random.nextInt(100);
    }

    /**
     * 外网图片转存判定，对于没有转存过的，且是http开头的网络图片时，才需要进行转存
     *
     * @param img
     * @return true 表示不需要转存
     */
    @Override
    public boolean uploadIgnore(String img) {
        if (StringUtils.isNotBlank(imageProperties.getCdnHost()) && img.startsWith(imageProperties.getCdnHost())) {
            return true;
        }

        return !img.startsWith("http");
    }
}

