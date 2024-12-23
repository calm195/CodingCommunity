package cor.chrissy.community.web.front.notice.vo;

import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.notify.dto.NotifyMsgDTO;
import lombok.Data;

import java.util.Map;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Data
public class NoticeResVo {
    /**
     * 消息通知列表
     */
    private PageListVo<NotifyMsgDTO> list;

    /**
     * 每个分类的未读数量
     */
    private Map<String, Integer> unreadCountMap;

    /**
     * 当前选中的消息类型
     */
    private String selectType;
}

