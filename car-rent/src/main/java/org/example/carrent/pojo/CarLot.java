package org.example.carrent.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
// 表示车辆停车场的实体类
public class CarLot {
    // 设置数据库的主键自增
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.AUTO)
    Long lotId; // 停车场ID
    String lotName; // 停车场名称
    Double x; // 停车场经度
    Double y; // 停车场纬度
    Long capacity; // 停车场容量
    Long state; // 停车场状态
}
