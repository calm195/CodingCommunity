package cor.chrissy.community.web.hook.interceptor;

import cor.chrissy.community.web.config.GlobalViewConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wx128
 * @date 2024/12/9
 */
@Slf4j
@Component
public class GlobalViewInterceptor implements AsyncHandlerInterceptor {
    @Resource
    private GlobalViewConfig globalViewConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 重定向请求不需要添加
        if (!ObjectUtils.isEmpty(modelAndView)) {
            modelAndView.getModel().put("siteInfo", globalViewConfig);
            modelAndView.getModel().put("isLogin", true);
            UserInfo userInfo = new UserInfo().setUid(1L).setUname("chrissy")
                    .setAvatar("https://blog.hhui.top/hexblog/images/avatar.jpg")
                    .setNewMsgList(Collections.singletonList(new UserMsg().setMsgId(100L).setMsgType(1).setMsg("模拟通知消息")));
            modelAndView.getModel().put("user", userInfo);
        }
    }

    @Data
    @Accessors(chain = true)
    private static class UserInfo {
        private Long uid;
        private String uname;
        private String avatar;
        private String role;
        private List<UserMsg> newMsgList;
    }

    @Data
    @Accessors(chain = true)
    private static class UserMsg {
        private long msgId;
        private int msgType;
        private String msg;
    }
}

