package cor.chrissy.community.service.image.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import cor.chrissy.community.core.config.ImageProperties;
import cor.chrissy.community.core.util.Md5Util;
import jdk.jfr.events.ExceptionStatisticsEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author wx128
 * @createAt 2025/1/3
 */
@Slf4j
@Getter
@Setter
@Component
@ConditionalOnExpression(value = "#{'ali'.equals(environment.getProperty('image.oss.type'))}")
public class AliOssWrapper implements IOssUploader, InitializingBean, DisposableBean {
    private static final int SUCCESS_CODE = 200;
    @Resource
    private ImageProperties properties;
    private OSS ossClient;

    public String upload(InputStream input, String fileType) {
        try {
            // 创建PutObjectRequest对象。
            byte[] bytes = StreamUtils.copyToByteArray(input);

            return upload(bytes, fileType);
        } catch (OSSException ossException) {
            log.error("Oss rejected with an error response! msg:{}, code: {}, reqId: {}, host:{}",
                    ossException.getMessage(),
                    ossException.getErrorCode(),
                    ossException.getRequestId(),
                    ossException.getHostId());
            return null;
        } catch (Exception e) {
            log.error("Caught an ClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with OSS, " +
                    "such as not being able to access the network.： {}", e.getMessage());
            return null;
        }
    }

    public String upload(byte[] bytes, String fileType) {
        try {
            String fileName = Md5Util.encode(bytes);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            fileName = properties.getOss().getPrefix() + fileName + "." + getFileType(inputStream, fileType);

            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getOss().getBucket(), fileName, inputStream);
            putObjectRequest.setProcess("true");
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
            if (SUCCESS_CODE == putObjectResult.getResponse().getStatusCode()) {
                return properties.getOss().getHost() + fileName;
            } else {
                log.error("upload to oss error. code:{}", putObjectResult.getResponse().getStatusCode());
                return null;
            }
        } catch (OSSException ossException) {
            log.error("Oss rejected with an error response! msg:{}, code: {}, reqId: {}, host:{}",
                    ossException.getMessage(),
                    ossException.getErrorCode(),
                    ossException.getRequestId(),
                    ossException.getHostId());
            return null;
        } catch (Exception e) {
            log.error("Caught an ClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with OSS, " +
                    "such as not being able to access the network.： {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean uploadIgnore(String fileUrl) {
        if (StringUtils.isNotBlank(properties.getOss().getHost()) && fileUrl.startsWith(properties.getOss().getHost())) {
            return true;
        }

        return !fileUrl.startsWith("http");
    }

    @Override
    public void destroy() throws Exception {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建OSSClient实例。
        ossClient = new OSSClientBuilder().build(properties.getOss().getEndpoint(), properties.getOss().getAk(), properties.getOss().getSk());
    }
}

