package cor.chrissy.community.service.statistics.service;

/**
 * @author wx128
 * @createAt 2025/1/14
 */
public interface UserStatisticService {
    /**
     * 添加在线人数
     *
     * @param add 正数，表示添加在线人数；负数，表示减少在线人数
     * @return
     */
    int incrOnlineUserCnt(int add);

    /**
     * 查询在线用户人数
     *
     * @return
     */
    int getOnlineUserCnt();
}