package org.example.carrent.service;



import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    PageItem<User> queryByPage(Integer current, Integer size);
    PageItem<User> queryByPageCond(Integer pageNum, Integer pageSize, User query);

    boolean deleteUser(Long id);

    boolean updateUser(User user);

    User checkUser(User user);

    User getUserWithId( Long id);
    boolean updateUserPassword(User user);

    boolean register(User user);

}
