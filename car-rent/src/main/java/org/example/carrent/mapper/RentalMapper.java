package org.example.carrent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.carrent.pojo.Rental;

@Mapper
public interface RentalMapper extends BaseMapper<Rental> {
}
