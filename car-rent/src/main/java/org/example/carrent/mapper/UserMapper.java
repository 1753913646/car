package org.example.carrent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.carrent.pojo.User;

import java.math.BigDecimal;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 查询性别统计数据
    @Select("SELECT " +
            "SUM(CASE WHEN sex = '男' THEN 1 ELSE 0 END) AS maleCount, " +
            "SUM(CASE WHEN sex = '女' THEN 1 ELSE 0 END) AS femaleCount " +
            "FROM user")
    Map<String, BigDecimal> getGenderStatistics();
}
