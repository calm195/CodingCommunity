package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.service.user.service.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "admin/user/")
public class UserSettingRestController {

    @Autowired
    private UserSettingService userSettingService;
}