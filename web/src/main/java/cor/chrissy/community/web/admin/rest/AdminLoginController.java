package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.account.service.LoginService;
import cor.chrissy.community.service.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author wx128
 * @createAt 2025/1/4
 */
@RestController
@RequestMapping(path = {"/api/admin/login", "/admin/login"})
public class AdminLoginController {

    @Autowired
    private UserService userService;

    @Qualifier("pwdLoginServiceImpl")
    @Autowired
    private LoginService loginService;

    @PostMapping(path = {"", "/"})
    public Result<BaseUserInfoDTO> login(HttpServletRequest request,
                                         HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        BaseUserInfoDTO info = userService.passwordLogin(username, password);
        String session = loginService.login(info.getUserId());
        if (StringUtils.isNotBlank(session)) {
            // cookie中写入用户登录信息
            response.addCookie(new Cookie(LoginService.SESSION_KEY, session));
            return Result.ok(info);
        } else {
            return Result.fail(StatusEnum.LOGIN_FAILED_MIXED, "登录失败，请重试");
        }
    }

    @Permission(role = UserRole.LOGIN)
    @RequestMapping("/logout")
    public Result<Boolean> logOut(HttpServletResponse response) throws IOException {
        Optional.ofNullable(ReqInfoContext.getReqInfo()).ifPresent(s -> loginService.logout(s.getSession()));
        // 重定向到首页
        response.sendRedirect("/");
        return Result.ok(true);
    }
}

