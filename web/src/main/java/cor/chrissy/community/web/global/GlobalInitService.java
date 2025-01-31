package cor.chrissy.community.web.global;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.context.Seo;
import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.core.util.JsonUtil;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.account.service.LoginService;
import cor.chrissy.community.service.notify.service.NotifyService;
import cor.chrissy.community.service.statistics.service.UserStatisticService;
import cor.chrissy.community.web.config.GlobalViewConfig;
import cor.chrissy.community.web.global.vo.GlobalVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Slf4j
@Service
public class GlobalInitService {
    @Value("${env.name}")
    private String env;
    @Autowired
    @Qualifier("pwdLoginServiceImpl")
    private LoginService loginService;

    @Resource
    private GlobalViewConfig globalViewConfig;

    @Resource
    private NotifyService notifyService;

    @Resource
    private SeoInjectService seoInjectService;

    @Resource
    private UserStatisticService userStatisticService;

    /**
     * 全局属性配置
     */
    public GlobalVo globalAttr() {
        GlobalVo vo = new GlobalVo();
        vo.setEnv(env);
        vo.setSiteInfo(globalViewConfig);
        vo.setOnlineCnt(userStatisticService.getOnlineUserCnt());

        if (ReqInfoContext.getReqInfo() == null
            || ReqInfoContext.getReqInfo().getSeo() == null
            || CollectionUtils.isEmpty(ReqInfoContext.getReqInfo().getSeo().getOgp())) {
            Seo seo = seoInjectService.defaultSeo();
            vo.setOgp(seo.getOgp());
            vo.setJsonLd(JsonUtil.toStr(seo.getJsonLd()));
        } else {
            Seo seo = ReqInfoContext.getReqInfo().getSeo();
            vo.setOgp(seo.getOgp());
            vo.setJsonLd(JsonUtil.toStr(seo.getJsonLd()));
        }

        try {
            if (ReqInfoContext.getReqInfo() != null && NumUtil.upZero(ReqInfoContext.getReqInfo().getUserId())) {
                vo.setIsLogin(true);
                vo.setUser(ReqInfoContext.getReqInfo().getUserInfo());
                vo.setMsgNum(ReqInfoContext.getReqInfo().getMsgNum());
            } else {
                vo.setIsLogin(false);
            }

            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            if (request.getRequestURI().startsWith("/column")) {
                vo.setCurrentDomain("column");
            } else {
                vo.setCurrentDomain("article");
            }
        } catch (Exception e) {
            log.error("loginCheckError:", e);
        }
        return vo;
    }

    /**
     * 初始化用户信息
     *
     * @param reqInfo
     */
    public void initLoginUser(ReqInfoContext.ReqInfo reqInfo) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request.getCookies() == null) {
            return;
        }
        for (Cookie cookie : request.getCookies()) {
            if (LoginService.SESSION_KEY.equalsIgnoreCase(cookie.getName())) {
                String session = cookie.getValue();
                BaseUserInfoDTO user = loginService.getAndUpdateUserIpInfoBySessionId(session, reqInfo.getClientIp());
                reqInfo.setSession(session);
                if (user != null) {
                    reqInfo.setUserId(user.getUserId());
                    reqInfo.setUserInfo(user);
                    reqInfo.setMsgNum(notifyService.queryUserNotifyMsgCount(user.getUserId()));
                }
                return;
            }
        }
    }
}
