package org.example.carrent.service;

import org.example.carrent.pojo.CarLot;
import org.example.carrent.pojo.PageItem;

import java.util.List;

 // 服务层  租赁点服务接口
public interface CarLotService {
    // 根据分页条件查询租车点车量信息
    PageItem<CarLot> queryByPageCond(Integer pageNum, Integer pageSize, CarLot query);
    // 根据id删除租车点信息
    boolean deleteCarLot(List<String> ids);
    // 插入租车点信息
    boolean insertCarLot(CarLot carLot);
    // 修改租车点信息
    boolean updateCarLot(CarLot carLot);
}
