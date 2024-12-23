package cor.chrissy.community.web.account.rest;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.user.wx.WxTxtMsgReq;
import cor.chrissy.community.common.req.user.wx.WxTxtMsgRes;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.account.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author wx128
 * @createAt 2024/12/16
 */
@Controller
public class LoginRestController {
    @Autowired
    private LoginService loginService;

    @ResponseBody
    @RequestMapping("login")
    public Result<Boolean> login(@RequestParam(name = "code") String code, HttpServletResponse response) {
        String session = loginService.login(code);
        if (StringUtils.isNotBlank(session)) {
            response.addCookie(new Cookie(LoginService.SESSION_KEY, session));
            return Result.ok(true);
        } else {
            return Result.fail(StatusEnum.LOGIN_FAILED_MIXED, "登录码异常，请重新输入");
        }
    }

    @ResponseBody
    @Permission(role = UserRole.LOGIN)
    @RequestMapping("logout")
    public Result<Boolean> logOut() {
        Optional.ofNullable(ReqInfoContext.getReqInfo()).ifPresent(s -> loginService.logout(s.getSession()));
        return Result.ok(true);
    }
}

