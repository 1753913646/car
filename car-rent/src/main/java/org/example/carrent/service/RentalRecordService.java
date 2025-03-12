package org.example.carrent.service;

import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.RentalRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RentalRecordService {
    // 查询所有租赁记录
    PageItem<RentalRecord> queryByPageCond(Integer pageNum, Integer pageSize, RentalRecord query);

    // 删除当条租赁记录
    boolean deleteRentalRecord(String rentalId);

    // 批量删除多条租赁记录
    boolean deleterentalRecords(List<RentalRecord> rentalIds);


    // 修改编辑租赁记录
    boolean updateRentalRecord(RentalRecord rentalId);

}
