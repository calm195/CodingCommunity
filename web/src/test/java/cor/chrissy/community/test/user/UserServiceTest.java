package cor.chrissy.community.test.user;

import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.service.user.service.UserService;
import cor.chrissy.community.test.BasicTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
public class UserServiceTest extends BasicTest {

    @Autowired
    private UserService userService;

    /**
     * 注册一个用户
     */
    @Test
    public void testRegister() {
        UserSaveReq req = new UserSaveReq();
        req.setThirdAccountId(UUID.randomUUID().toString());
        req.setLoginType(0);
        userService.registerOrGetUserInfo(req);
        long userId = req.getUserId();

        UserInfoSaveReq save = new UserInfoSaveReq();
        save.setUserId(userId);
        save.setUserName("一灰灰");
        save.setPhoto("https://spring.hhui.top/spring-blog/css/images/avatar.jpg");
        save.setCompany("xm");
        save.setPosition("java");
        save.setProfile("码农");
        userService.saveUserInfo(save);
    }

}
