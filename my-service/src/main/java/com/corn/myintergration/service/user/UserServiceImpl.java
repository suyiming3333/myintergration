package com.corn.myintergration.service.user;

import com.corn.myintergration.entity.User;
import com.corn.myintergration.dao.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: suyiming
 * @Date: 18-11-12 23:35
 * @Description:
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> list() {
        return userDao.findAll();
    }
}
