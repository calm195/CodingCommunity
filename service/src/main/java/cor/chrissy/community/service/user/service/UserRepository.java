package cor.chrissy.community.service.user.service;

/**
 * @author wx128
 * @createAt 2024/12/15
 */
public interface UserRepository {
    /**
     * 查询关注用户总数
     *
     * @param userId
     * @return
     */
    Long queryUserFollowCount(Long userId);

    /**
     * 查询粉丝总数
     *
     * @param userId
     * @return
     */
    Long queryUserFansCount(Long userId);
}
