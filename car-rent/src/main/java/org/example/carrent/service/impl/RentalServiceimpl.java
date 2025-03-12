package org.example.carrent.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.carrent.mapper.RentalMapper;
import org.example.carrent.mapper.UserMapper;
import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.Rental;
import org.example.carrent.pojo.User;
import org.example.carrent.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalServiceimpl implements RentalService {
    @Autowired
    RentalMapper rentalMapper;

    @Autowired
    UserMapper userMapper;

    @Override

    // 查询
    public PageItem<Rental> queryByPageCond(Integer currentPage, Integer pageSize, Rental query) {
        Page<Rental> page = new Page<>(currentPage, pageSize);
        // 进行一个条件查询
        LambdaQueryWrapper<Rental> lqw = new LambdaQueryWrapper<>();
        lqw.like(query.getCarId() !=null,Rental::getCarId,query.getCarId()); // 根据车辆ID模糊查询
        lqw.like(query.getUserId() !=null,Rental::getUserId,query.getUserId()); // 根据用户ID模糊查询
        rentalMapper.selectPage(page, lqw);
        // 进行用户信息的查询
        List<Rental> rentalList = page.getRecords();
        for (Rental rental : rentalList) {
            User user = userMapper.selectById(rental.getUserId());
            rental.setUser(user);
        }
        PageItem<Rental> pi = new PageItem<>(rentalList, page.getTotal());
        rentalList.forEach(System.out::println);
        return pi;
    }

    @Override
    public boolean deleteRental(String id) {
        Rental rental = rentalMapper.selectById(Long.valueOf(id));
        if (rental == null) {
            return false;
        }
        int ret = rentalMapper.deleteById(Long.valueOf(id));
        if (ret == 1){
            // 删除语句成功
            return true;
        } else {
            return false;
        }
    }


    // 批量删除
    @Override
    public boolean deleteRecords(List<Rental> rentalList) {
        for (Rental rental:rentalList){
            Rental r1=rentalMapper.selectById(rental.getId());
            if (r1==null){
                return false;
            }
        }
        for (Rental rental:rentalList){
            int ret=rentalMapper.deleteById(rental.getId());
            if (ret<0){
                return false;
            }
        }
        return true;
    }


    // 修改信息
    @Override
    public int updateRecord(Rental rental) {
        Rental r = rentalMapper.selectById(rental.getId());
        if (r == null) {
            // 用户不存在， 返回修改失败
            return 0;
        }
        // 修改
        int res = rentalMapper.updateById(rental);
        if (res == 1) {
            // 修改成功
            return 1;
        }
        return 0;
    }

}
