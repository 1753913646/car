package org.example.carrent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.carrent.mapper.CarLotMapper;

import org.example.carrent.pojo.CarLot;
import org.example.carrent.pojo.PageItem;
import org.example.carrent.service.CarLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarLotServiceImpl implements CarLotService {

    @Autowired
    CarLotMapper carLotMapper;

    @Override
    public PageItem<CarLot> queryByPageCond(Integer currentPage, Integer pageSize, CarLot query) {
        Page<CarLot> page = new Page<>(currentPage, pageSize);
        // 进行一个条件查询
        LambdaQueryWrapper<CarLot> lqw = new LambdaQueryWrapper<>();
        // 用户 id 进行模糊查询
        lqw.like(query.getLotName() != null, CarLot::getLotName, query.getLotName());
        lqw.like(query.getX() != null, CarLot::getX, query.getX());
        //  执行分页查询
        carLotMapper.selectPage(page, lqw);
        // page 对象就是分页结果
        List<CarLot> carLotList = page.getRecords();
        // 创建一个 pageItem对象
        PageItem<CarLot> pi = new PageItem<>(carLotList, page.getTotal());
        carLotList.forEach(System.out::println);
        return pi;
    }


    // 删除功能  删除指定的租车点根据租车点信息
    @Override
    public boolean deleteCarLot(List<String> lotIds) {
        for (String lotId : lotIds) {
            CarLot carLot = carLotMapper.selectById(Long.valueOf(lotId));
            if (carLot == null) {
                // 如果某个ID不存在，则返回false
                return false;
            }
            int ret = carLotMapper.deleteById(Long.valueOf(lotId));
            if (ret != 1){
                // 如果某个ID删除失败，则返回false
                return false;
            }
        }
        // 如果所有ID都删除成功，则返回true
        return true;
    }


    //插入功能  插入租赁点信息
    @Override
    public boolean insertCarLot(CarLot carLot) {
        int res = carLotMapper.insert(carLot); // 插入一条记录
        if (res == 1){
            return true;
        }
        return  false;
    }

    //更新功能 更新(修改)车位信息
    @Override
    public boolean updateCarLot(CarLot carLot) {
        // 查询是否存在该租赁点的记录
        CarLot res = carLotMapper.selectById(carLot.getLotId());
        if (res == null){
            return false;
        }
        int cr = carLotMapper.updateById(carLot);
        if (cr == 1) {
            // 修改成功
            return true;
        }
        return false;
    }
}
