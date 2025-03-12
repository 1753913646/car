package com.chinasoft.cr;

import com.chinasoft.cr.mapper.CarMapper;
import com.chinasoft.cr.mapper.UserMapper;

import com.chinasoft.cr.pojo.User;
import com.chinasoft.cr.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;

@SpringBootTest
class CarRentApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CarMapper carMapper;

    @Test
    void contextLoads() {
        User user = userMapper.selectById(15L);

        user.setId(null);
        userMapper.insert(user);
    }




    @Test
    void testGetToken() {
        User user = userMapper.selectById(15L);
        System.out.println(user);
        String token = JwtUtils.createToken(user.getId().toString(), user.getUsername());
        System.out.println(token);

        boolean flag = JwtUtils.verifyToken(token);
        System.out.println(flag);
    }

}
