package org.example.carrent.controller;

import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.Rental;
import org.example.carrent.pojo.Result;
import org.example.carrent.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class RentalController {
    @Autowired
    RentalService rentalService;


//    网页向控制层发送请求，然后控制层调用业务逻辑层的服务（服务层），业务逻辑层的服务完成数据库操作，然后返回结果给控制层，控制层再把结果返回给网页。

    // 查询功能
    @GetMapping("/api/carwithuser")
    public Result queryUserPage(Integer pageNum, Integer pageSize, Rental query) {
        System.out.println(query);
        PageItem<Rental> pi = rentalService.queryByPageCond(pageNum, pageSize, query);
        Result result = new Result(200, "查询成功 ！", pi);
        return result;
    }

    // 单项删除
    @PostMapping("/api/carwithuser/deleteRecord")
    public Result deleteRental(@RequestBody Map<String, String> payload){
        String id = payload.get("id");
        boolean flag = rentalService.deleteRental(id);
        if (flag){
            return new Result(200,"删除成功！");
        } else {
            return new Result(500,"删除失败，用户不存在！");
        }
    }


    // 批量删除
    @PostMapping("/api/carwithuser/deleteRecords")
    public Result deleteRecords(@RequestBody List<Rental> rentalList){
        boolean result = rentalService.deleteRecords(rentalList);
        if (result) {
            return new Result(200,"删除成功");
        } else {
            return new Result(404, "删除失败！");
        }
    }

    // 修改信息
    @PostMapping("/api/carwithuser")
    public Result updateRecord(@RequestBody Rental id) {
        int result = rentalService.updateRecord(id);
        if (result > 0) {
            return new Result(200,"修改成功");
        } else {
            return new Result(404, "修改失败！");
        }
    }



}
