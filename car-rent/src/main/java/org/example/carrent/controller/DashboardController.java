package org.example.carrent.controller;

import org.example.carrent.pojo.DashboardData;
import org.example.carrent.pojo.Result;
import org.example.carrent.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class DashboardController {
    @Autowired
    DashboardService dashboardService;

    //dashboard数据获取
    @GetMapping("/api/dashboard")
    public Result getDashboardData() {
        DashboardData data = dashboardService.getDashboardData();
        return new Result(200, "success", data);
    }
}
