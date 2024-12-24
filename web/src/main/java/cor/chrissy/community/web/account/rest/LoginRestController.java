package cor.chrissy.community.web.account.rest;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.user.wx.WxTxtMsgReq;
import cor.chrissy.community.common.req.user.wx.WxTxtMsgRes;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.account.service.LoginService;
import cor.chrissy.community.web.account.helper.QrLoginHelper;
import cor.chrissy.community.web.account.vo.QrLoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author wx128
 * @createAt 2024/12/16
 */
@RestController
@RequestMapping
public class LoginRestController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private QrLoginHelper qrLoginHelper;

    @Deprecated
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
    public Result<Boolean> logOut(HttpServletResponse response) throws Exception {
        Optional.ofNullable(ReqInfoContext.getReqInfo()).ifPresent(s -> loginService.logout(s.getSession()));
        response.sendRedirect("/");
        return Result.ok(true);
    }
    
     /**
     * 获取登录的验证码
     *
     * @return
     */
    @GetMapping(path = "/login/code")
    public Result<QrLoginVo> qrLogin(HttpServletRequest request, HttpServletResponse response) {
        QrLoginVo vo = new QrLoginVo();
        vo.setCode(qrLoginHelper.genVerifyCode(request, response));
        return Result.ok(vo);
    }


    /**
     * 刷新验证码
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping(path = "/login/refresh")
    public Result<QrLoginVo> refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        QrLoginVo vo = new QrLoginVo();
        String code = qrLoginHelper.refreshCode(request, response);
        if (StringUtils.isBlank(code)) {
            // 刷新失败，之前的连接已失效，重新建立连接
            code = qrLoginHelper.genVerifyCode(request, response);
            vo.setCode(code);
            vo.setReconnect(true);
        } else {
            vo.setCode(code);
            vo.setReconnect(false);
        }
        return Result.ok(vo);
    }
}

