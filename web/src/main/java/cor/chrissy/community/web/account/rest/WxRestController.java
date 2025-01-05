package cor.chrissy.community.web.account.rest;

import cor.chrissy.community.common.req.user.wx.BaseWxMsgRes;
import cor.chrissy.community.common.req.user.wx.WxTxtMsgReq;
import cor.chrissy.community.common.req.user.wx.WxTxtMsgRes;
import cor.chrissy.community.service.account.service.LoginService;
import cor.chrissy.community.web.account.helper.QrLoginHelper;
import cor.chrissy.community.web.account.helper.WxHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("pwdLoginServiceImpl")
    private LoginService loginService;
    @Autowired
    private QrLoginHelper qrLoginHelper;
    @Autowired
    private WxHelper wxHelper;

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
    public BaseWxMsgRes callBack(@RequestBody WxTxtMsgReq msg) {
        String content = msg.getContent();
        if ("subscribe".equals(msg.getEvent()) || "scan".equalsIgnoreCase(msg.getEvent())) {
            // 关注公众号
            String key = msg.getEventKey();
            if (StringUtils.isNotBlank(key) || key.startsWith("qrscene_")) {
                // 带参数的二维码，扫描、关注事件拿到之后，直接登录，省却输入验证码这一步
                // fixme 带参数二维码需要 微信认证，个人公众号无权限
                String code = key.substring("qrscene_".length());
                String verifyCode = loginService.getVerifyCode(msg.getFromUserName());
                qrLoginHelper.login(code, verifyCode);
                WxTxtMsgRes res = new WxTxtMsgRes();
                res.setContent("登录成功");
                fillRes(res, msg);
                return res;
            }
        }
        BaseWxMsgRes res = wxHelper.buildResponseBody(msg.getEvent(), content, msg.getFromUserName());
        fillRes(res, msg);
        return res;
    }

    private void fillRes(BaseWxMsgRes res, WxTxtMsgReq msg) {
        res.setFromUserName(msg.getFromUserName());
        res.setCreateTime(msg.getCreateTime());
        res.setToUserName(msg.getToUserName());
    }
}
