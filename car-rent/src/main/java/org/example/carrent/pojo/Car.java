package org.example.carrent.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("car")
public class Car {
    @TableId(type = IdType.AUTO)

    private Long id; // 车辆ID
    private String licensePlate; // 车牌号
    private String model; // 车型
    private Double pricePerDay; // 每日租金
    private String manufacturer; // 制造商
    private Integer productionYear; // 生产年份
    private String status; // 状态：0 - 未出租；1 - 已出租
    private Integer rentalCount; // 此车被租次数

    // getters and setters
}