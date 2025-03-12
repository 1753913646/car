package org.example.carrent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.china.carrent.pojo.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.carrent.pojo.Car;

import java.util.List;
import java.util.Map;

@Mapper
public interface CarMapper extends BaseMapper<Car> {
    @Select("SELECT COUNT(*) FROM car WHERE status = 1")
    int countLendCars();  // 查询 status 为 1 的车辆数量

    // 查询每个汽车品牌的租赁总数
    @Select("SELECT model, SUM(rental_count) AS total_rental_count FROM car GROUP BY model")
    List<Map<String, Object>> getBrandRentalStatistics();

}
