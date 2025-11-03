package top.mhxi.myshop.common.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 自定义异常类
@Data
@AllArgsConstructor
public class MyShopException extends RuntimeException {
    private Integer code;
    private String msg;
}
