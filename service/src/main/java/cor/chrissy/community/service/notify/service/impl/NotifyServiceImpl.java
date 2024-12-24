package cor.chrissy.community.service.notify.service.impl;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.NotifyStatEnum;
import cor.chrissy.community.common.enums.NotifyTypeEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.notify.dto.NotifyMsgDTO;
import cor.chrissy.community.service.notify.repository.dao.NotifyMsgDao;
import cor.chrissy.community.service.notify.service.NotifyService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class NotifyServiceImpl implements NotifyService {
    @Resource
    private NotifyMsgDao notifyMsgDao;

    @Override
    public int queryUserNotifyMsgCount(Long userId) {
        return notifyMsgDao.countByUserIdAndStat(userId, NotifyStatEnum.UNREAD.getStat());
    }

    /**
     * 查询消息通知列表
     *
     * @return
     */
    public PageListVo<NotifyMsgDTO> queryUserNotices(Long userId, NotifyTypeEnum type, PageParam page) {
        List<NotifyMsgDTO> list = notifyMsgDao.listNotifyMsgByUserIdAndType(userId, type, page);
        if (!CollectionUtils.isEmpty(list)) {
            notifyMsgDao.updateNotifyMsgToRead(list);
            return PageListVo.newVo(list, page.getPageSize());
        }
        return PageListVo.emptyVo();
    }

    @Override
    public Map<String, Integer> queryUnreadCounts(long userId) {
        Map<Integer, Integer> map = Collections.emptyMap();
        if (ReqInfoContext.getReqInfo() != null && NumUtil.upZero(ReqInfoContext.getReqInfo().getMsgNum())) {
            map = notifyMsgDao.groupCountByUserIdAndStat(userId, NotifyStatEnum.UNREAD.getStat());
        }
        // 指定先后顺序
        Map<String, Integer> ans = new LinkedHashMap<>();
        initCnt(NotifyTypeEnum.COMMENT, map, ans);
        initCnt(NotifyTypeEnum.REPLY, map, ans);
        initCnt(NotifyTypeEnum.PRAISE, map, ans);
        initCnt(NotifyTypeEnum.COLLECT, map, ans);
        initCnt(NotifyTypeEnum.FOLLOW, map, ans);
        initCnt(NotifyTypeEnum.SYSTEM, map, ans);
        return ans;
    }

    private void initCnt(NotifyTypeEnum type, Map<Integer, Integer> map, Map<String, Integer> result) {
        result.put(type.name().toLowerCase(), map.getOrDefault(type.getType(), 0));
    }
}
