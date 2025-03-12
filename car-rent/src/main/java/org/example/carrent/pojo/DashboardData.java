package org.example.carrent.pojo;

import lombok.Data;

import java.util.List;

@Data
public class DashboardData {
    Integer lendRecordCount;
    Integer visitCount;
    Integer carCount;
    Integer userCount;

    List<Integer> weeklyCarRentals;  // 每周租赁汽车的数量
    List<String> carBrands;          // 汽车品牌
    List<Integer> brandRentals;      // 每个品牌的租赁数量
    Integer maleCount;                 // 男性用户数量
    Integer femaleCount;               // 女性用户数量
}
