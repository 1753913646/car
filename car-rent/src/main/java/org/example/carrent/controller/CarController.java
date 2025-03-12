package org.example.carrent.controller;

//import com.chinasoft.cr.pojo.Car;
//import com.chinasoft.cr.pojo.PageItem;
//import com.chinasoft.cr.pojo.Result;
//import com.chinasoft.cr.service.CarService;

import org.example.carrent.pojo.Car;
import org.example.carrent.pojo.PageItem;
import org.example.carrent.pojo.Result;
import org.example.carrent.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin   // 解决跨域问题
public class CarController {

    @Autowired
    CarService carService;

    @PostMapping("/api/cars/number/{currentPage}/size/{pageSize}" )
    public Result queryCarPage(@PathVariable("currentPage") Integer currentPage, @PathVariable("pageSize")Integer pageSize, @RequestBody Car query) {
        PageItem<Car> pi = carService.queryByPageCond(currentPage, pageSize, query);
        Result result = new Result(200, "查询成功 ！", pi);
        return result;



    }

    @PostMapping("/api/car/add")
    public Result addCar(@RequestBody Car car) {
        // 设置状态为已上架
//        car.setStatus("1");

        // 新增车辆
        boolean success = carService.addCar(car);
        if (success) {
            return new Result(200, "上架车辆成功", null);
        } else {
            return new Result(500, "上架车辆失败", null);
        }
    }



    @DeleteMapping("/api/car/{id}")
    public Result deleteCar(@PathVariable Long id) {
        System.out.println("car id is " + id);
        boolean flag = carService.deleteCar(id);
        if (flag == true) {
            return new Result(200, "删除成功！");
        } else {
            return new Result(404, "删除失败，用户不存在！");
        }
    }

    @PutMapping("/api/car")
    public Result updateCar(
            // 将 json字符串转换为 pojo对象
            @RequestBody Car car)  {
        System.out.println("--------------------update--------------------------");
        System.out.println(car);
        Boolean flag = carService.updateCar(car);

        if (flag == true) {
            return new Result(200, "修改成功！");
        } else {
            return new Result(404, "修改失败，该车辆不存在！");
        }

    }


    @PostMapping("api/car/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        try {
            carService.deleteBatch(ids);
            return new Result(200, "批量删除成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(500, "批量删除失败: " + e.getMessage(), null);
        }
    }

}