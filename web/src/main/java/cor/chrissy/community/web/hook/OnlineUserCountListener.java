package cor.chrissy.community.web.hook;

import cor.chrissy.community.core.util.SpringUtil;
import cor.chrissy.community.service.statistics.service.UserStatisticService;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author wx128
 * @createAt 2025/1/14
 */
@WebListener
public class OnlineUserCountListener implements HttpSessionListener {


    /**
     * 新增session，在线人数统计数+1
     *
     * @param se
     */
    public void sessionCreated(HttpSessionEvent se) {
        HttpSessionListener.super.sessionCreated(se);
        SpringUtil.getBean(UserStatisticService.class).incrOnlineUserCnt(1);
    }

    /**
     * session失效，在线人数统计数-1
     *
     * @param se
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSessionListener.super.sessionDestroyed(se);
        SpringUtil.getBean(UserStatisticService.class).incrOnlineUserCnt(-1);
    }
}

