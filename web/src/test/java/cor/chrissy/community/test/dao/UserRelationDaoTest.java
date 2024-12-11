package cor.chrissy.community.test.dao;

import cor.chrissy.community.common.enums.FollowStateEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.user.UserRelationReq;
import cor.chrissy.community.service.user.dto.UserFollowListDTO;
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

        UserRelationReq req1 = new UserRelationReq();
        req1.setUserId(1L);
        req1.setFollowUserId(2L);
        req1.setFollowState(FollowStateEnum.FOLLOW.getCode());
        userRelationService.saveUserRelation(req1);

        UserRelationReq req2 = new UserRelationReq();
        req2.setUserId(1L);
        req2.setFollowUserId(3L);
        req2.setFollowState(FollowStateEnum.FOLLOW.getCode());
        userRelationService.saveUserRelation(req2);

        UserRelationReq req3 = new UserRelationReq();
        req3.setUserId(2L);
        req3.setFollowUserId(1L);
        req3.setFollowState(FollowStateEnum.FOLLOW.getCode());
        userRelationService.saveUserRelation(req3);
    }

    @Test
    public void testCancelUserRelation() throws Exception {
        UserRelationReq req = new UserRelationReq();
        req.setUserRelationId(1L);
        req.setFollowState(FollowStateEnum.CANCEL_FOLLOW.getCode());
        userRelationService.saveUserRelation(req);
    }

    @Test
    public void testUserRelation() {
        UserFollowListDTO userFollowListDTO = userRelationService.getUserFollowList(1L, PageParam.newPageInstance(1L, 10L));
        log.info("query userFollowDTOS: {}", userFollowListDTO);

        UserFollowListDTO userFansListDTO = userRelationService.getUserFansList(1L, PageParam.newPageInstance(1L, 10L));
        log.info("query userFansList: {}", userFansListDTO);
    }
}
