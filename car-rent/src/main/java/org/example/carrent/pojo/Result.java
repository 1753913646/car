package org.example.carrent.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    Integer status;
    String message;
    Object data;

    public Result(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}
