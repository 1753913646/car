package org.example.carrent.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("access")
public class Access {
    @TableId
    Long id;
    Integer num;
}
