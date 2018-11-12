package com.corn.myintergration.controller.user;

import com.corn.myintergration.dao.user.common.R;
import com.corn.myintergration.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: suyiming
 * @Date: 18-11-12 23:19
 * @Description:
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userServiceImpl;

    @GetMapping("/list")
    public R list(){
        try {
            return R.isOk().data(userServiceImpl.list());
        } catch (Exception e) {
            return R.isFail(e);
        }
    }
}
