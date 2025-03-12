package org.example.carrent.service;


import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.Rental;

import java.util.List;

public interface RentalService {

    // 查询所有记录
    PageItem<Rental> queryByPageCond(Integer currentPage, Integer pageSize, Rental query);

    // 删除一条记录
    boolean deleteRental(String id);

    // 批量删除
    boolean deleteRecords(List<Rental> rentalList);

     // 编辑修改信息
    int updateRecord(Rental id);


}
