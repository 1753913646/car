package org.example.carrent;

import org.example.carrent.mapper.UserMapper;
import org.example.carrent.pojo.User;
import org.example.carrent.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
        User user = userMapper.selectById(22l);
        System.out.println(user);
        String token = JwtUtils.createToken(user.getId().toString(), user.getUsername());
        System.out.println(token);
        if(JwtUtils.verifyToken(token)){
            System.out.println("令牌是有效的");
        };
    }






}
