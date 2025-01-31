package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.user.dto.SimpleUserInfoDTO;
import cor.chrissy.community.service.user.service.UserService;
import cor.chrissy.community.service.user.service.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@Permission(role = UserRole.LOGIN)
@RequestMapping(path = {"/api/admin/user", "/admin/user/"})
public class UserSettingRestController {

    @Autowired
    private UserSettingService userSettingService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/query")
    public Result<List<SimpleUserInfoDTO>> queryUserList(String name) {
        return Result.ok(userService.searchUser(name));
    }
}