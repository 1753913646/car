package org.example.carrent.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.chinasoft.cr.mapper.CarMapper;
//import com.chinasoft.cr.pojo.Car;
//import com.chinasoft.cr.pojo.PageItem;
//
//import com.chinasoft.cr.service.CarService;
import org.example.carrent.mapper.CarMapper;
import org.example.carrent.pojo.Car;
import org.example.carrent.pojo.PageItem;
import org.example.carrent.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarServicempl implements CarService {

    @Autowired
    CarMapper carMapper;


    @Override
    public PageItem<Car> queryByPageCond(Integer currentPage, Integer pageSize, Car query) {

        Page<Car> page = new Page<>(currentPage, pageSize);
        // 进行一个条件查询
        LambdaQueryWrapper<Car> lqw = new LambdaQueryWrapper<>();

        // 用户 id 进行模糊查询
//        if (query.getId() != null) {
//            lqw.like(User::getId, query.getId());
//        }
        lqw.like(query.getLicensePlate() != null, Car::getLicensePlate, query.getLicensePlate());
        lqw.like(query.getModel() != null, Car::getModel, query.getModel());
        lqw.like(query.getManufacturer() != null, Car::getManufacturer, query.getManufacturer());

        //  执行分页查询
        carMapper.selectPage(page, lqw);

        // page 对象就是分页结果
        List<Car> carList = page.getRecords();
        // 创建一个 pageItem对象
        PageItem<Car> pi = new PageItem<>(carList, page.getTotal());
        carList.forEach(System.out::println);

        return pi;
    }



    @Override
    public boolean deleteCar(Long id) {

        Car car = carMapper.selectById(id);
        if (car == null)  {
            //  用户不存在
            return false;
        }
        // 用户存在
        int ret = carMapper.deleteById(id);
        if (ret == 1) {
            // 删除语句执行成功
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Boolean updateCar(Car car) {
        Car c1 = carMapper.selectById(car.getId());

        if (c1 == null) {
            // 该车辆不存在， 返回修改失败
            return false;
        }

        // 修改
        int ret = carMapper.updateById(car);
        if (ret == 1) {
            // 修改成功
            return true;
        }
        // 修改失败了
        return false;
    }


    @Override
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        for (Long id : ids) {
            carMapper.deleteById(id);
        }
    }
    @Override
    @Transactional
    public boolean addCar(Car car) {
        // 插入数据
        int result = carMapper.insert(car);
        return result > 0; // 返回插入是否成功
    }




    // 其他方法...
}





