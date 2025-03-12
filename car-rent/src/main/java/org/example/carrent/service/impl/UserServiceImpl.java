package org.example.carrent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.carrent.mapper.UserMapper;
import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.User;
import org.example.carrent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public PageItem<User> queryByPage(Integer current, Integer size) {
        // 创建一个分页对象（也是一个结果）
        //  当前是第一页， 每页有  5 条记录
        Page<User> page = new Page<>(current, size);
        //  执行分页查询
        userMapper.selectPage(page, null);

        // page 对象就是分页结果
        List<User> userList = page.getRecords();
        // 创建一个 pageItem对象
        PageItem<User> pi = new PageItem<>(userList, page.getTotal());

        return pi;
    }

    @Override
    public PageItem<User> queryByPageCond(Integer pageNum, Integer pageSize, User query) {
        // 创建一个分页对象（也是一个结果）
        //  当前是第一页， 每页有  5 条记录
        Page<User> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();

        lqw.like(query.getId() != null, User::getId, query.getId());
        lqw.like(query.getUsername() != null, User::getUsername, query.getUsername());
        lqw.like(query.getPhone() != null, User::getPhone, query.getPhone());
        lqw.like(query.getAddress() != null, User::getAddress, query.getAddress());

        //  执行分页查询
        userMapper.selectPage(page, lqw);

        // page 对象就是分页结果
        List<User> userList = page.getRecords();

        PageItem<User> pi = new PageItem<>(userList,page.getTotal());
        userList.forEach(System.out::println);
        return pi;
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return false;
        }
        int ret = userMapper.deleteById(id);
        if (ret == 1) {
            // 删除成功
            return true;
        } else {
            // 删除失败
            return false;
        }
    }


    @Override
    public boolean updateUser(User user) {
        // 查询
        User u1 = userMapper.selectById(user.getId());

        if (u1 == null) {
            // 用户不存在， 返回修改失败
            return false;
        }

        // 修改
        int ret = userMapper.updateById(user);
        if (ret == 1) {
            // 修改成功
            return true;
        }
        // 修改失败了
        return false;
    }

    /**
     *  进行用户账号和密码的校验
     * @param user：username和password
     * @return
     *   null： 登录失败
     *   user对象： 登录成功
     */
    @Override
    public User checkUser(User user) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        // 设置查询条件
        lqw.eq(User::getUsername, user.getUsername());
        // 执行查询
        User userDb = userMapper.selectOne(lqw);
        // 对密码进行判断
        if (user.getPassword().equals(userDb.getPassword())) {
            // 登录成功
            return userDb;
        }
        // 登录失败
        return null;
    }

    @Override
    public User getUserWithId(Long id) {
        return userMapper.selectById(id);
    }


    // 用户密码修改
    @Override
    public boolean updateUserPassword(User user) {
        User u1 = userMapper.selectById(user.getId());
        if (u1 == null) {
            return false;
        }
        if (u1.getPassword().equals(user.getPassword())){
            return false;
        }
        String password = user.getPassword();
        user.setPassword(password);
        this.updateById(user);
        return true;
    }

    @Override
    public boolean register(User user) {
        // 检查用户名是否已经存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            // 用户名已存在，注册失败
            return false;
        }

        // 保存用户信息到数据库
        int result = userMapper.insert(user);
        return result > 0;
    }

}
