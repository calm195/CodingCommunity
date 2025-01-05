package cor.chrissy.community.web.front.image.rest;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.image.service.ImageService;
import cor.chrissy.community.web.front.image.vo.ImageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    @RequestMapping(path = "upload")
    public Result<ImageVo> upload(HttpServletRequest request) {
        ImageVo imageVo = new ImageVo();
        try {
            String imagePath = imageServiceImpl.saveImg(request);
            imageVo.setImagePath(imagePath);
        } catch (Exception e) {
            log.error("save upload file error! e: {}", e.toString());
            return Result.fail(StatusEnum.UPLOAD_PIC_FAILED);
        }
        return Result.ok(imageVo);
    }

    @RequestMapping(path = "save")
    public Result<ImageVo> save(@RequestParam(name = "img", defaultValue = "") String imgUrl) {
        ImageVo imageVo = new ImageVo();
        if (StringUtils.isBlank(imgUrl)) {
            return Result.ok(imageVo);
        }

        String url = imageServiceImpl.saveImg(imgUrl);
        imageVo.setImagePath(url);
        return Result.ok(imageVo);
    }
}
