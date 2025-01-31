package cor.chrissy.community.service.image.service.impl;

import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.base.constants.MediaType;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.core.util.ExceptionUtil;
import cor.chrissy.community.core.util.MdImgLoader;
import cor.chrissy.community.service.image.oss.IOssUploader;
import cor.chrissy.community.service.image.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private IOssUploader ossUploader;

    private static final MediaType[] STATIC_IMG_TYPE = new MediaType[]{MediaType.ImagePng, MediaType.ImageJpg, MediaType.ImageWebp, MediaType.ImageGif};

    private final LoadingCache<String, String> imgReplaceCache = CacheBuilder.newBuilder()
            .maximumSize(300)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                @NotNull
                @Override
                public String load(@NotNull String img) {
                    try {
                        InputStream stream = FileReadUtil.getStreamByFileName(img);
                        URI uri = URI.create(img);
                        String path = uri.getPath();
                        int index = path.lastIndexOf(".");
                        String fileType = null;
                        if (index > 0) {
                            fileType = path.substring(index + 1);
                        }
                        return ossUploader.upload(stream, fileType);
                    } catch (Exception e) {
                        log.error("外网照片转存异常，image：{}。exception: {}", img, e.toString());
                        return "";
                    }
                }
            });

    @Override
    public String saveImg(HttpServletRequest request) {

        MultipartFile file = null;
        if (request instanceof MultipartHttpServletRequest) {
            file = ((MultipartHttpServletRequest) request).getFile("image");
        }

        if (file == null) {
            log.error("load upload image error! The image url is empty!");
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "it's no image here");
        }

        // 目前只支持 jpg, png, webp 等静态图片格式
        String fileType = validateStaticImg(file.getContentType());
        if (fileType == null) {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "no supported fileType");
        }

        // 获取BufferedImage对象
        try {
            return ossUploader.upload(file.getInputStream(), fileType);
        } catch (IOException e) {
            log.error("Parse image from httpRequest to BufferedImage error!  not supported fileType: {}", e.toString());
            throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED);
        }
    }

    @Override
    public String mdImgReplace(String content) {
        List<MdImgLoader.MdImg> imgList = MdImgLoader.loadImgs(content);
        for (MdImgLoader.MdImg img : imgList) {
            String newImg = saveImg(img.getUrl());
            content = StringUtils.replace(content, img.getOrigin(), "![" + img.getDesc() + "](" + newImg + ")");
        }
        return content;
    }

    /**
     * 外网图片转存
     *
     * @param img
     * @return
     */
    @Override
    public String saveImg(String img) {
        if (ossUploader.uploadIgnore(img)) {
            return img;
        }

        try {
            String ans = imgReplaceCache.get(img);
            if (StringUtils.isBlank(ans)) {
                return buildUploadFailImgUrl(img);
            }
            return ans;
        } catch (Exception e) {
            return buildUploadFailImgUrl(img);
        }
    }

    private String buildUploadFailImgUrl(String img) {
        return img.contains("saveError") ? img : img + "?&cause=saveError!";
    }

    /**
     * 图片格式校验
     *
     * @param mime
     * @return
     */
    private String validateStaticImg(String mime) {
        if ("svg".equalsIgnoreCase(mime)) {
            return "svg";
        }
        if (mime == null) {
            return null;
        }
        if (mime.contains(MediaType.ImageJpg.getExt())) {
            mime = mime.replace("jpg", "jpeg");
        }
        for (MediaType type : STATIC_IMG_TYPE) {
            if (type.getMime().equals(mime)) {
                return type.getExt();
            }
        }
        return null;
    }
}

