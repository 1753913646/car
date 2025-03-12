package org.example.carrent.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class User {
    // 设置数据库的主键自增
    @JsonFormat(shape = JsonFormat.Shape.STRING)
            @TableId(type = IdType.AUTO)

    Long id;
    String username;
    String password;
    String nickName;
    String phone;
    String sex;
    String address;
    Integer role;
}
