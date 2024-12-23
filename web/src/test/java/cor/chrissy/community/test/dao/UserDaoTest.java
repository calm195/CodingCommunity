package cor.chrissy.community.test.dao;

import cor.chrissy.community.service.user.dto.UserStatisticInfoDTO;
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
    public void testUserHome() {
        UserStatisticInfoDTO userStatisticInfoDTO = userService.queryUserInfoWithStatistic(1L);
        log.info("query userPageDTO: {}", userStatisticInfoDTO);
    }
}
