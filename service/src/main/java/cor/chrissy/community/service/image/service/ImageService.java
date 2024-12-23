package cor.chrissy.community.service.image.service;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface ImageService {

    /**
     * 获取图片
     *
     * @param request
     * @return
     */
    BufferedImage getImg(HttpServletRequest request);

    /**
     * 图片本地保存
     *
     * @param bf
     * @return
     */
    String saveImg(BufferedImage bf);
}

