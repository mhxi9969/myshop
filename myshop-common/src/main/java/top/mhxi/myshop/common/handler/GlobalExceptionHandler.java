package top.mhxi.myshop.common.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.mhxi.myshop.common.utils.R;

//全局异常处理
@ControllerAdvice
@Slf4j  // 写到日志
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)  //指定处理异常的种类，所有异常
    @ResponseBody
    public R error(Exception e) { //出现异常，会调用这个方法
        e.printStackTrace();
        return R.error().message("出现异常，执行全局异常处理");
    }


    @ExceptionHandler(MyShopException.class)  //指定处理异常的种类，特定的异常
    @ResponseBody
    public R error(MyShopException e) {
        //写到日志中
        log.error(e.getMessage());
        return R.error().code(e.getCode()).message(e.getMsg());
    }

    // 处理 @Valid @RequestBody 校验失败
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseBody
    public R handleMethodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();
        log.error("参数校验失败: {}", msg);
        return R.error().message(msg);
    }

    // 处理 @Validated 普通参数校验失败
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    @ResponseBody
    public R handleConstraintViolationException(javax.validation.ConstraintViolationException e) {
        log.error("参数校验失败: {}", e.getMessage());
        return R.error().message(e.getMessage());
    }
}
