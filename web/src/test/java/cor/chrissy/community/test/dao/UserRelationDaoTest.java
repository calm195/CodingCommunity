package cor.chrissy.community.test.dao;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.user.dto.FollowUserInfoDTO;
import cor.chrissy.community.service.user.service.UserRelationService;
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
public class UserRelationDaoTest extends BasicTest {

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserService userService;

    @Test
    public void saveUserRelation() throws Exception {

    }

    @Test
    public void testCancelUserRelation() throws Exception {
    }

    @Test
    public void testUserRelation() {
        PageListVo<FollowUserInfoDTO> userFollowListDTO = userRelationService.getUserFollowList(1L, PageParam.newPageInstance(1L, 10L));
        log.info("query userFollowDTOS: {}", userFollowListDTO);

        PageListVo<FollowUserInfoDTO> userFansListDTO = userRelationService.getUserFansList(1L, PageParam.newPageInstance(1L, 10L));
        log.info("query userFansList: {}", userFansListDTO);
    }
}
