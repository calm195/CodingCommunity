package cor.chrissy.community.service.notify.service;

import cor.chrissy.community.common.enums.NotifyTypeEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.notify.dto.NotifyMsgDTO;

import java.util.Map;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface NotifyService {
    /**
     * 查询用户未读消息数量
     *
     * @param userId
     * @return
     */
    int queryUserNotifyMsgCount(Long userId);

    /**
     * 查询通知列表
     *
     * @param userId
     * @param type
     * @param page
     * @return
     */
    PageListVo<NotifyMsgDTO> queryUserNotices(Long userId, NotifyTypeEnum type, PageParam page);

    /**
     * 查询未读消息数
     *
     * @param userId
     * @return
     */
    Map<String, Integer> queryUnreadCounts(long userId);
}
