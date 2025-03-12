package org.example.carrent.controller;


import lombok.Data;
import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.Result;
import org.example.carrent.pojo.RentalRecord;
import org.example.carrent.service.RentalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Data
@RestController
@CrossOrigin   // 解决跨域问题
public class RentalRecordController {

    @Autowired
    RentalRecordService rentalRecordService;


    // 查询所有记录
    @GetMapping("/api/RentalRecord")
    public Result queryRentalRecordPage(Integer pageNum, Integer pageSize, RentalRecord query) {
        System.out.println(query);
        PageItem<RentalRecord> pi = rentalRecordService.queryByPageCond(pageNum, pageSize, query);
        Result result = new Result(200, "查询成功", pi);
        return result;
    }

    // 根据id单项删除
    @PostMapping("/api/RentalRecord/deleteRecord")
    public  Result deleteRental(@RequestBody String rentalId){
        boolean flag = rentalRecordService.deleteRentalRecord(rentalId);
        if (flag == true) {
            return new Result(200, "删除成功！");
        } else {
            return new Result(404, "删除失败，订单不存在！");
        }
    }


    // 编辑（修改）信息
    @PutMapping("api/RentalRecord/{rentalId}")
    public Result updateRentalRecord(@RequestBody RentalRecord rentalId){
        System.out.println("rentalId is " + rentalId);
        boolean flag=rentalRecordService.updateRentalRecord(rentalId);
        if (flag == true){
            return new Result(200, "更新成功！");
        } else {
            return new Result(400, "更新失败！");
        }
    }


    //批量删除
    @PostMapping("/api/LendRecord/deleteRecords")
    public Result deleteRentalRecords(@RequestBody List<RentalRecord> rentalRecord){
        boolean flag=rentalRecordService.deleterentalRecords(rentalRecord);
        if (flag){
            return  new Result(200,"删除成功！");
        } else {
            return  new Result(404,"删除失败！");
        }
    }



}