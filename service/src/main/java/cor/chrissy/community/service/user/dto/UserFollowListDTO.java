package cor.chrissy.community.service.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
public class UserFollowListDTO {

    /**
     * 用户列表
     */
    List<UserFollowDTO> userFollowList;

    /**
     * 是否有更多
     */
    private Boolean isMore;
}
