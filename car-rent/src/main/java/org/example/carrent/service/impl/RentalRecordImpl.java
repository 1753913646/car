package org.example.carrent.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.carrent.mapper.RentalRecordMapper;
import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.RentalRecord;
import org.example.carrent.service.RentalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalRecordImpl implements RentalRecordService {

    @Autowired
    RentalRecordMapper rentalRecordMapper;

    // 查询所有租车记录
    @Override
    public PageItem<RentalRecord> queryByPageCond(Integer pageNum, Integer pageSize, RentalRecord query) {
        // 创建一个分页对象（也是一个结果）
        //  当前是第一页， 每页有  5 条记录
        Page<RentalRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<RentalRecord> lqw = new LambdaQueryWrapper<>();
        lqw.like(query.getRentalId() != null, RentalRecord::getRentalId, query.getRentalId());
        lqw.like(query.getCarId() != null, RentalRecord::getCarId, query.getCarId());
        lqw.like(query.getUserId() != null, RentalRecord::getUserId, query.getUserId());
        //  执行分页查询
        rentalRecordMapper.selectPage(page, lqw);
        // page 对象就是分页结果
        List<RentalRecord> rentalRecordList = page.getRecords();
        PageItem<RentalRecord> petter = new PageItem<>(rentalRecordList, page.getTotal());
        rentalRecordList.forEach(System.out::println);
        return petter;
    }

    // 删除单条租车记录
    @Override
    public boolean deleteRentalRecord(String rentalId) {
        RentalRecord rentalRecord = rentalRecordMapper.selectById(rentalId);
        if (rentalRecord == null){
            return false;
        }
        int ret = rentalRecordMapper.deleteById(rentalId);
        if (ret == 1){
            return true;
        }else{
            return  false;
        }
    }

    // 修改编辑租车记录
    @Override
    public boolean updateRentalRecord(RentalRecord rentalId) {
        RentalRecord res = rentalRecordMapper.selectById(rentalId.getRentalId());
        if (res == null){
            return false;
        }
        // 将获取到的id转化为整形
        int inter = rentalRecordMapper.updateById(rentalId);
        if (inter == 1){
            return true;
        }
        return false;
    }

    // 批量删除租车记录
    @Override
    public boolean deleterentalRecords(List<RentalRecord> rentalIds) {
        for (RentalRecord rn : rentalIds){
            RentalRecord records=rentalRecordMapper.selectById(rn.getRentalId());
            if (records == null) {
                return false;
            }
        }
        boolean yes = true;
        for (RentalRecord rn : rentalIds){
            int ret=rentalRecordMapper.deleteById(rn.getRentalId());
            if(ret != 1){
                yes = false;
            }
        }
        return yes;
    }

}
