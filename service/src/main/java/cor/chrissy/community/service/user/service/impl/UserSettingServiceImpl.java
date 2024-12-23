package cor.chrissy.community.service.user.service.impl;

import cor.chrissy.community.service.user.repository.dao.UserDao;
import cor.chrissy.community.service.user.service.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class UserSettingServiceImpl implements UserSettingService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer getUserCount() {
        return userDao.getUserCount();
    }
}
