package org.example.carrent.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.example.carrent.pojo.CarLot;
import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.Result;
import org.example.carrent.service.CarLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Data
@RestController
@CrossOrigin   // 解决跨域问题
public class CarLotController {

    @Autowired
    CarLotService carLotService;


    // 查询所有车库信息  处理获取汽车库分页查询的请求
    @GetMapping("/api/carLot/page")
    public Result queryUserPage(Integer current, Integer size, CarLot query){
        PageItem<CarLot> pi = carLotService.queryByPageCond(current, size, query);
        Result result = new Result(200, "查询成功 ！", pi);
        return result;
    }


    // 根据id删除车库信息
    @PostMapping("/api/carLot/delByIds")
    public Result deleteCarLot(@RequestBody List<String> lotIds){
        boolean flag = carLotService.deleteCarLot(lotIds);
        if (flag){
            return new Result(200,"删除成功！");
        } else {
            return new Result(500,"删除失败！");
        }
    }

    // 新增租赁点信息
    @PostMapping("/api/carLot")
    public Result insertCarLot(@RequestBody CarLot carLot){
        boolean result = carLotService.insertCarLot(carLot);
        if (result){
            return new Result(200,"新增成功");
        }else {
            return new Result(400,"新增失败");
        }
    }


    @PutMapping("/api/carLot")
    public Result updateCarLot(@RequestBody CarLot carLotId){
        boolean result = carLotService.updateCarLot(carLotId);
        if (result){
            return new Result(200,"修改成功");
        }else {
            return new Result(400,"修改失败");
        }
    }

}