package cor.chrissy.community.web.hook.interceptor;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.user.service.UserService;
import cor.chrissy.community.web.config.GlobalViewConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 注入全局配置信息，如站点信息，thymleaf
 *
 * @author wx128
 * @createAt 2024/12/9
 */
@Slf4j
@Component
public class GlobalViewInterceptor implements AsyncHandlerInterceptor {
    @Resource
    private GlobalViewConfig globalViewConfig;

    @Resource
    private UserService userService;

    @Value("${env.name}")
    private String env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Permission permission = handlerMethod.getMethod().getAnnotation(Permission.class);
            if (permission == null) {
                permission = handlerMethod.getBeanType().getAnnotation(Permission.class);
            }

            if (permission == null || permission.role() == UserRole.ALL) {
                return true;
            }
            if (ReqInfoContext.getReqInfo() == null || ReqInfoContext.getReqInfo().getUserId() == null) {
                response.sendRedirect("403");
                return false;
            }

            if (permission.role() == UserRole.ADMIN && !"admin".equalsIgnoreCase(ReqInfoContext.getReqInfo().getUserInfo().getRole())) {
                response.sendRedirect("403");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 重定向请求不需要添加
        if (!ObjectUtils.isEmpty(modelAndView)) {
            modelAndView.getModel().put("env", env);
            modelAndView.getModel().put("siteInfo", globalViewConfig);

            if (ReqInfoContext.getReqInfo() == null || ReqInfoContext.getReqInfo().getUserId() == null) {
                modelAndView.getModel().put("isLogin", false);

            } else {
                modelAndView.getModel().put("isLogin", true);
                modelAndView.getModel().put("user", userService.getUserHomeDTO(ReqInfoContext.getReqInfo().getUserId()));
                // 消息数 fixme 消息信息改由消息模块处理
                modelAndView.getModel().put("msgs", Arrays.asList(new UserMsg().setMsgId(100L).setMsgType(1).setMsg("模拟通知消息")));
            }
        }
    }

    @Data
    @Accessors(chain = true)
    private static class UserMsg {
        private long msgId;
        private int msgType;
        private String msg;
    }
}

