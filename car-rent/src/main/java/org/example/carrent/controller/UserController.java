package org.example.carrent.controller;


import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.Result;
import org.example.carrent.pojo.User;
import org.example.carrent.service.UserService;
import org.example.carrent.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RestController
@CrossOrigin   // 解决跨域问题
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/api/user/usersearch")
    public Result queryUserPage(Integer pageNum, Integer pageSize,User query) {
        System.out.println(query);
        PageItem<User> pi = userService.queryByPageCond(pageNum, pageSize, query);
        Result result = new Result(200, "查询成功", pi);
        return result;
    }


    @DeleteMapping("/api/user/{id}")
    public Result deleteUser(@PathVariable Long id) {
        System.out.println("user id is " + id);
        boolean flag = userService.deleteUser(id);
        if (flag == true) {
            return new Result(200, "删除成功！");
        } else {
            return new Result(404, "删除失败，用户不存在！");
        }
    }

    @PutMapping("/api/user")
    public Result updateUser(@RequestBody User user)  {
        boolean flag = userService.updateUser(user);
        if (flag) {
            return new Result(200, "修改成功！");
        } else {
            return new Result(404, "修改失败，用户不存在！");
        }
    }

    @PostMapping("api/user/login")
    public Result login(@RequestBody User user) {
        User dbUser = userService.checkUser(user);
        System.out.println(user);
        System.out.println(dbUser);
        if (dbUser != null) {
            String token = JwtUtils.createToken(dbUser.getId().toString(),dbUser.getUsername());
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("user", dbUser);
            return new Result(200, "登录成功！",map);
        } else {
            return new Result(500, "登录失败！");
        }
    }


    @GetMapping("/api/user/info")
    public Result getUserInfo(@RequestHeader("Authorization") String token) {
        System.out.println("--------------------info----------------------");
        System.out.println(token);
        if (JwtUtils.verifyToken(token)) {
            System.out.println("令牌是 ok");
            String strId = JwtUtils.getAudience(token);
            System.out.println(strId);
            Long userId = Long.parseLong(strId);
            // 根据id查询用户的信息
            User user = userService.getUserWithId(userId);
            System.out.println(user);
            return new Result(200, "用户信息获取成功！", user);
        }
        return new Result(400, "无效令牌！");
    }

    //修改密码
    @PutMapping("/api/user/password")
    public Result updateUserPassword(@RequestBody User user) {
        boolean flag = userService.updateUserPassword(user);
        if (flag) {
            return new Result(200, "success");
        }else{
            return new Result(404, "failed");
        }
    }

    //注册
    @PostMapping("/api/user/register")
    public Result register(@RequestBody User user) {
        boolean flag = userService.register(user);
        if (flag) {
            return new Result(200, "success");
        } else {
            return new Result(404, "failed");
        }
    }

}