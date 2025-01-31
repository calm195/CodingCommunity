package cor.chrissy.community.web.account.helper;

import cor.chrissy.community.common.req.user.wx.BaseWxMsgRes;
import cor.chrissy.community.common.req.user.wx.WxImgTxtItem;
import cor.chrissy.community.common.req.user.wx.WxImgTxtMsgRes;
import cor.chrissy.community.common.req.user.wx.WxTxtMsgRes;
import cor.chrissy.community.core.util.CodeGenerateUtil;
import cor.chrissy.community.core.util.JsonUtil;
import cor.chrissy.community.core.util.MapUtil;
import cor.chrissy.community.service.account.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Slf4j
@Component
public class WxHelper {
    // todo replace by my url
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx4a128c315d9b1228&secret=077e2d92dee69f04ba6d53a0ef4459f9";

    public static final String QR_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    /**
     * 访问token
     */
    public static volatile String token = "";

    /**
     * 失效时间
     */
    public static volatile long expireTime = 0L;

    @Autowired
    @Qualifier("pwdLoginServiceImpl")
    private LoginService loginService;

    @Autowired
    private QrLoginHelper qrLoginHelper;

    private final RestTemplate restTemplate;

    public WxHelper() {
        restTemplate = new RestTemplate();
    }

    private synchronized void doGetToken() {
        ResponseEntity<HashMap> entity = restTemplate.getForEntity(ACCESS_TOKEN_URL, HashMap.class);
        HashMap data = entity.getBody();
        log.info("getToken:{}", JsonUtil.toStr(entity));
        token = (String) data.get("access_token");
        int expire = (int) data.get("expires_in");
        // 提前至十分钟失效
        expireTime = System.currentTimeMillis() / 1000 + expire - 600;
    }

    public String autoUpdateAccessToken() {
        if (StringUtils.isBlank(token) || System.currentTimeMillis() / 1000 >= expireTime) {
            doGetToken();
        }
        return token;
    }

    /**
     * 获取带参数的登录二维码地址
     *
     * @param code
     * @return
     */
    public String getLoginQrCode(String code) {
        String url = QR_CREATE_URL + autoUpdateAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> params = MapUtil.createHashMap("action_name", "QR_LIMIT_SCENE",
                "expire_seconds", 300,
                "action_info", MapUtil.createHashMap("scene", MapUtil.createHashMap("scene_str", code)));
        HttpEntity<String> request = new HttpEntity<>(JsonUtil.toStr(params), headers);

        Map ans = restTemplate.postForObject(url, request, HashMap.class);
        String qrcode = (String) ans.get("url");
        return qrcode;
    }

    /**
     * 返回自动响应的文本
     *
     * @return
     */
    public BaseWxMsgRes buildResponseBody(String eventType, String content, String fromUser) {
        // 返回的文本消息
        String textRes = null;
        // 返回的是图文消息
        List<WxImgTxtItem> imgTxtList = null;
        if ("subscribe".equalsIgnoreCase(eventType)) {
            // 订阅
            textRes = "优秀的你一关注，楼仔英俊的脸上就泛起了笑容[奸笑]。我这个废柴，既可以把程序人生写得风趣幽默，也可以把技术文章写得通俗易懂。\n" +
                    "\n" +
                    "这里整理了一份「 2023年超硬核面试备战手册」，内容涵盖计算机网络、操作系统、数据结构与算法、MySQL、Redis、Java、Spring、高并发等等\n" +
                    "\n" +
                    "<a href=\"https://mp.weixin.qq.com/s/szctSvy3dG3dyP4AY6MsuA\">[勾引]速来，手慢无！</a>\n" +
                    "\n" +
                    "我从清晨走过，也拥抱夜晚的星辰，人生没有捷径，你我皆平凡，你好，陌生人，一起共勉。\n";
        }
        // 下面是关键词回复
        else if ("110".equalsIgnoreCase(content)) {
            textRes = "[10 本校招/社招必刷八股文] 链接: https://pan.baidu.com/s/1-ElSmMtaHXSl9bj8lChXQA?pwd=iw20 提取码: iw20";
        } else if ("119".equalsIgnoreCase(content) || "高并发".equalsIgnoreCase(content)) {
            textRes = "[高并发手册] 链接: https://pan.baidu.com/s/15UuFz__trjW2iLGugUiCIw?pwd=wwlm 提取码: wwlm";
        } else if ("120".equalsIgnoreCase(content) || "JVM".equalsIgnoreCase(content)) {
            textRes = "[JVM手册] 链接: https://pan.baidu.com/s/1b-YD5hbPNdJsWeEQTw7TSA?pwd=h66t 提取码: h66t";
        } else if ("122".equalsIgnoreCase(content) || "Spring".equalsIgnoreCase(content)) {
            textRes = "[Spring源码解析手册] 链接: https://pan.baidu.com/s/1gww69GapzScKRVseSq2lpg?pwd=6kvt 提取码: 6kvt";
        } else if ("资料".equalsIgnoreCase(content) || "pdf".equalsIgnoreCase(content) || "楼仔".equalsIgnoreCase(content)) {
            textRes = "链接: https://pan.baidu.com/s/1mGkHxsWQPOlySIZm7i9FbA?pwd=0mje 提取码: 0mje\n";
        }
        // 下面是回复图文消息
        else if ("加群".equalsIgnoreCase(content)) {
            WxImgTxtItem imgTxt = new WxImgTxtItem();
            imgTxt.setTitle("扫码加群");
            imgTxt.setDescription("加入技术交流群，定期分享技术好文，卷起来！");
            imgTxt.setPicUrl("https://mmbiz.qpic.cn/mmbiz_jpg/sXFqMxQoVLGOyAuBLN76icGMb2LD1a7hBCoialjicOMsicvdsCovZq2ib1utmffHLjVlcyAX2UTmHoslvicK4Mg71Kyw/0?wx_fmt=jpeg");
            imgTxt.setUrl("https://mp.weixin.qq.com/s?__biz=Mzg3OTU5NzQ1Mw==&mid=2247489777&idx=1&sn=fe41b1d5b461213c1586befc602618ac&chksm=cf035a13f874d305c21c7dbdcc6ce0f8ffbc59c1fa2f8d436620017a676881d175b2f0af3306&token=466180380&lang=zh_CN#rd");
            imgTxtList = Arrays.asList(imgTxt);
        }
        // 微信公众号登录
        else if (CodeGenerateUtil.isVerifyCode(content)) {
            String verifyCode = loginService.autoRegisterAndGetVerifyCode(fromUser);
            if (qrLoginHelper.login(content, verifyCode)) {
                textRes = "登录成功";
            } else {
                textRes = "验证码过期了，刷新登录页面重试一下吧";
            }
        }

        if (textRes != null) {
            WxTxtMsgRes vo = new WxTxtMsgRes();
            vo.setContent(textRes);
            return vo;
        } else {
            WxImgTxtMsgRes vo = new WxImgTxtMsgRes();
            vo.setArticles(imgTxtList);
            vo.setArticleCount(imgTxtList.size());
            return vo;
        }
    }
}

