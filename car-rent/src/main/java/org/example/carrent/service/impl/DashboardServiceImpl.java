package org.example.carrent.service.impl;

import org.example.carrent.mapper.AccessMapper;
import org.example.carrent.mapper.CarMapper;
import org.example.carrent.mapper.UserMapper;
import org.example.carrent.pojo.Access;
import org.example.carrent.pojo.DashboardData;
import org.example.carrent.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    CarMapper carMapper;
    @Autowired
    AccessMapper accessMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public DashboardData getDashboardData() {
        DashboardData dashboardData = new DashboardData();

        // 获取 status 为 1 的车辆数量
        int lendRecordCount = carMapper.countLendCars();
        dashboardData.setLendRecordCount(lendRecordCount);

        // 获取 id 为 1 的访问记录并使其自增 +1
        Access access = accessMapper.selectById(1L);
        int visitCount = access.getNum();
        access.setNum(visitCount + 1);
        accessMapper.updateById(access);
        dashboardData.setVisitCount(visitCount + 1);

        // 获取车辆数量
        int carCount  = Math.toIntExact(carMapper.selectCount(null));
        dashboardData.setCarCount(carCount);

        // 获取用户数量
        int userCount = Math.toIntExact(userMapper.selectCount(null));
        dashboardData.setUserCount(userCount);

        // 添加周租赁汽车的热度数据 (模拟数据)
        List<Integer> weeklyCarRentals = Arrays.asList(150, 90, 260, 120, 185, 360, 280);
        dashboardData.setWeeklyCarRentals(weeklyCarRentals);

        // 获取汽车品牌租赁热度数据
        List<Map<String, Object>> brandRentalStats = carMapper.getBrandRentalStatistics();
        List<String> carBrands = brandRentalStats.stream()
                .map(stat -> (String) stat.get("model"))
                .collect(Collectors.toList());
        List<Integer> brandRentals = brandRentalStats.stream()
                .map(stat -> ((BigDecimal) stat.get("total_rental_count")).intValue())
                .collect(Collectors.toList());
        dashboardData.setCarBrands(carBrands);
        dashboardData.setBrandRentals(brandRentals);

        // 获取性别统计数据
        Map<String, BigDecimal> genderStats = userMapper.getGenderStatistics();
        dashboardData.setMaleCount(genderStats.get("maleCount").intValue());
        dashboardData.setFemaleCount(genderStats.get("femaleCount").intValue());

        return dashboardData;
    }
}
