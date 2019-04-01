package com.pay.tool.gokongpay.exception;

import com.pay.tool.gokongpay.model.ResponseData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice  //不指定包默认加了@Controller和@RestController都能控制
public class ExceptionControllerAdvice {
    /**
     * 全局异常处理，反正异常返回统一格式的
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseData exceptionHandler(Exception ex) {
        ResponseData responseData = new ResponseData();
        responseData.setCode(500);
        responseData.setMessage(ex.getMessage());
        return responseData;
    }
}
