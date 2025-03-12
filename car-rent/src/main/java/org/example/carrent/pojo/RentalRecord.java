package org.example.carrent.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RentalRecord {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.AUTO)
    Long rentalId; // 订单id
    Integer carId; // 车辆id
    Integer userId; // 用户id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date rentalStart;//订单起始日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date rentalEnd;// 订单截止日期
    String status;
    Integer rentalCount;
}

