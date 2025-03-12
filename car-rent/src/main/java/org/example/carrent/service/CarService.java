package org.example.carrent.service;

//import com.chinasoft.cr.pojo.PageItem;
//import com.chinasoft.cr.pojo.Car;


import org.example.carrent.pojo.Car;
import org.example.carrent.pojo.PageItem;

import java.util.List;


public interface CarService {
    PageItem<Car> queryByPageCond(Integer currentPage, Integer pageSize, Car query);

    boolean deleteCar(Long id);

    Boolean updateCar(Car car);


    void deleteBatch(List<Long> ids);

    boolean addCar(Car car);



}
