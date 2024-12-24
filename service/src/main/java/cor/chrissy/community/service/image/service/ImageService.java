package cor.chrissy.community.service.image.service;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface ImageService {

    /**
     * 图片转存
     * @param content
     * @return
     */
    String mdImgReplace(String content);

    /**
     * 外网图片转存
     *
     * @param img
     * @return
     */
    String saveImg(String img);

    /**
     * 上传图片转存
     *
     * @param request
     * @return
     */
    String saveImg(HttpServletRequest request);
}

