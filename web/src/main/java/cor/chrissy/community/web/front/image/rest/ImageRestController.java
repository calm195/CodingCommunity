package cor.chrissy.community.web.front.image.rest;

import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.image.service.ImageService;
import cor.chrissy.community.web.front.image.vo.ImageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Slf4j
@RestController
@RequestMapping(path = "image/")
public class ImageRestController {

    @Autowired
    private ImageService imageServiceImpl;

    /**
     * 图片上传
     *
     * @return
     */
    @ResponseBody
    @GetMapping(path = "upload")
    public Result<ImageVo> upload(HttpServletRequest request) {
        ImageVo imageVo = new ImageVo();
        try {
            BufferedImage img = imageServiceImpl.getImg(request);
            String imagePath = imageServiceImpl.saveImg(img);
            imageVo.setImagePath(imagePath);
        } catch (Exception e) {
            log.error("save upload file error! e: {}", e.toString());
        }
        return Result.ok(imageVo);
    }
}