package cor.chrissy.community.web.account.view;

import com.github.hui.quick.plugin.base.DomUtil;
import com.github.hui.quick.plugin.base.constants.MediaType;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.WriterException;
import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.web.account.helper.QrLoginHelper;
import cor.chrissy.community.web.account.helper.WxHelper;
import cor.chrissy.community.web.account.vo.QrLoginVo;
import cor.chrissy.community.web.config.GlobalViewConfig;
import cor.chrissy.community.web.global.BaseViewController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Controller
@Slf4j
public class LoginViewController extends BaseViewController {
    @Autowired
    private GlobalViewConfig globalViewConfig;
    @Autowired
    private QrLoginHelper qrLoginHelper;
    @Autowired
    private WxHelper wxHelper;

    /**
     * 这种是扫描二维码，输入[login/登录] 获取验证码，然后提交验证码来实现登录
     *
     * @return
     */
    @GetMapping(path = "login")
    public String login() {
        if (ReqInfoContext.getReqInfo().getUserId() != null) {
            // 已经登录时，直接跳转到主页
            return "/";
        }
        return "views/login/code";
    }

    /**
     * 二维码登录页面，有一个分配的验证码；然后用户扫描二维码关注公众号，输入验证码即可登录
     *
     * @param model
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @GetMapping(path = "qrLogin")
    public String wxLogin(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException, WriterException {
        QrLoginVo vo = new QrLoginVo();
        vo.setCode(qrLoginHelper.genVerifyCode(request, response));
//        下面这一行是准备做记录扫描二维码的状态，然而问题时重定向的二维码地址会被微信再次重定向到微信下载页
//        String qrUrl = globalViewConfig.getHost() + "/api/wxlogin?code=" + vo.getCode();
//        下面这一行是直接借助微信的带参数二维码来实现自动登录
//        String qrUrl = wxHelper.getLoginQrCode(vo.getCode());
        String qrUrl = globalViewConfig.getWxLoginUrl();
        String qrCode = QrCodeGenWrapper.of(qrUrl)
                .setW(400)
                .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE)
                .setDetectSpecial()
                .asString();
        vo.setQr(DomUtil.toDomSrc(qrCode, MediaType.ImagePng));
        model.addAttribute("vo", vo);
        return "views/login/wx";
    }

    /**
     * 客户端与后端建立扫描二维码的长连接
     *
     * @param id
     * @return
     */
    @GetMapping(path = "subscribe", produces = {org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter subscribe(String id) {
        return qrLoginHelper.subscribe(id);
    }

    /**
     * 扫描登录二维码，重定向到具体的微信公众号
     *
     * @param code
     * @param response
     * @throws IOException
     */
    @RequestMapping(path = "/api/wxlogin")
    public void redirect2wx(@RequestParam("code") String code,
                            HttpServletResponse response) throws IOException {
        qrLoginHelper.scan(code);
        String wx = globalViewConfig.getWxLoginUrl();
        log.info("redirect:{}", wx);
        response.sendRedirect(globalViewConfig.getWxLoginUrl());
    }
}
