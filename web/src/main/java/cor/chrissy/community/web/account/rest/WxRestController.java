package cor.chrissy.community.web.account.rest;

import cor.chrissy.community.common.req.user.wx.WxTxtMsgReq;
import cor.chrissy.community.common.req.user.wx.WxTxtMsgRes;
import cor.chrissy.community.service.account.service.LoginService;
import cor.chrissy.community.web.account.helper.QrLoginHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RequestMapping(path = "wx")
@RestController
public class WxRestController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private QrLoginHelper qrLoginHelper;

    /**
     * 微信的公众号接入 token 验证，即返回echostr的参数值
     *
     * @param request
     * @return
     */
    @GetMapping(path = "callback")
    public String check(HttpServletRequest request) {
        String echoStr = request.getParameter("echostr");
        if (StringUtils.isNoneEmpty(echoStr)) {
            return echoStr;
        }
        return "";
    }

    /**
     * fixme: 需要做防刷校验
     * 微信的响应返回
     * 本地测试访问: curl -X POST 'http://localhost:8080/wx/callback' -H 'content-type:application/xml' -d '<xml><URL><![CDATA[https://hhui.top]]></URL><ToUserName><![CDATA[一灰灰blog]]></ToUserName><FromUserName><![CDATA[demoUser1234]]></FromUserName><CreateTime>1655700579</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[login]]></Content><MsgId>11111111</MsgId></xml>' -i
     *
     * @param msg
     * @return
     */
    @PostMapping(path = "callback",
            consumes = {"application/xml", "text/xml"},
            produces = "application/xml;charset=utf-8")
    public WxTxtMsgRes callBack(@RequestBody WxTxtMsgReq msg) {
        String content = msg.getContent();
        WxTxtMsgRes res = new WxTxtMsgRes();
        res.setFromUserName(msg.getToUserName());
        res.setToUserName(msg.getFromUserName());
        res.setCreateTime(System.currentTimeMillis() / 1000);
        res.setMsgType("text");
        if ("subscribe".equals(msg.getEvent()) || "scan".equalsIgnoreCase(msg.getEvent())) {
            // 关注公众号
            String key = msg.getEventKey();
            if (StringUtils.isNotBlank(key) || key.startsWith("qrscene_")) {
                // 带参数的二维码，扫描、关注事件拿到之后，直接登录，省却输入验证码这一步
                // fixme 带参数二维码需要 微信认证，个人公众号无权限
                String code = key.substring("qrscene_".length());
                String verifyCode = loginService.getVerifyCode(msg.getFromUserName());
                qrLoginHelper.login(code, verifyCode);
                res.setContent("登录成功");
            } else {
                res.setContent("欢迎关注公众号!");
            }
        } else {
            if (loginSymbol(content)) {
                res.setContent("登录验证码: 【" + loginService.getVerifyCode(msg.getFromUserName()) + "】 五分钟内有效");
            } else if (NumberUtils.isDigits(content)) {
                String verifyCode = loginService.getVerifyCode(msg.getFromUserName());
                if (qrLoginHelper.login(content, verifyCode)) {
                    res.setContent("login success!");
                } else {
                    res.setContent("the code was outdated!");
                }
            } else {
                res.setContent("welcome!");
            }
        }
        return res;
    }

    /**
     * 判断是否为登录指令，后续扩展其他的响应
     *
     * @param msg
     * @return
     */
    private boolean loginSymbol(String msg) {
        if (StringUtils.isBlank(msg)) {
            return false;
        }

        msg = msg.trim();
        for (String key : LoginService.LOGIN_CODE_KEY) {
            if (msg.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }
}
