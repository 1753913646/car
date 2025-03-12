package org.example.carrent.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Rental {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.AUTO)
    Long id; // 订单id
    Long carId; // 车辆id
    Long userId; // 用户id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date rentalStart;//订单起始日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date rentalEnd;// 订单截止日期
    Integer extensionCount; // 此车被租次数
    @TableField(exist = false)
    User user; // 用户信息
}
