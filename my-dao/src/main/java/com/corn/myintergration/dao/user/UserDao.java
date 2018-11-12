package com.corn.myintergration.dao.user;

import com.corn.myintergration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: suyiming
 * @Date: 18-11-12 23:38
 * @Description:
 */
public interface UserDao extends JpaRepository<User,String> {
}
