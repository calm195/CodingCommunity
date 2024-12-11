package cor.chrissy.community.test.dao;

import cor.chrissy.community.common.req.user.UserInfoSaveReq;
import cor.chrissy.community.common.req.user.UserSaveReq;
import cor.chrissy.community.service.user.dto.UserHomeDTO;
import cor.chrissy.community.service.user.service.UserService;
import cor.chrissy.community.test.BasicTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Slf4j
public class UserDaoTest extends BasicTest {

    @Autowired
    private UserService userService;

    @Test
    public void testAddUser() throws Exception {
        UserSaveReq req = new UserSaveReq();
        req.setThirdAccountId("1234567");
        req.setLoginType(0);
        userService.saveUser(req);
        req.setThirdAccountId("12345677");
        req.setLoginType(1);
        userService.saveUser(req);
    }

    @Test
    public void testAddUserInfo() {
        UserInfoSaveReq req = new UserInfoSaveReq();
        req.setUserName("test");
        req.setUserId(2L);
        userService.saveUserInfo(req);

        req.setUserName("test");
        req.setUserId(3L);
        userService.saveUserInfo(req);
    }

    @Test
    public void testUserHome() throws Exception {
        UserHomeDTO userHomeDTO = userService.getUserHomeDTO(1L);
        log.info("query userPageDTO: {}", userHomeDTO);
    }
}
