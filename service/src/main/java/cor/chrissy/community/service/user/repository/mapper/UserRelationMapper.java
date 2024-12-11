package cor.chrissy.community.service.user.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.user.dto.UserFollowDTO;
import cor.chrissy.community.service.user.repository.entity.UserRelationDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface UserRelationMapper extends BaseMapper<UserRelationDO> {
    /**
     * 我关注的用户
     * @param followUserId
     * @param pageParam
     * @return
     */
    List<UserFollowDTO> queryUserFollowList(@Param("followUserId") Long followUserId, @Param("pageParam") PageParam pageParam);

    /**
     * 关注我的粉丝
     * @param userId
     * @param pageParam
     * @return
     */
    List<UserFollowDTO> queryUserFansList(@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);

    /**
     * 我关注的用户总数
     * @param followUserId
     * @param pageParam
     * @return
     */
    Integer queryUserFollowCount(@Param("followUserId") Long followUserId, @Param("pageParam") PageParam pageParam);

    /**
     * 关注我的粉丝总数
     * @param userId
     * @param pageParam
     * @return
     */
    Integer queryUserFansCount(@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);

}

