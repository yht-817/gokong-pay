package com.pay.tool.gokongpay.web;


import com.pay.tool.gokongpay.model.ResponseData;
import com.pay.tool.gokongpay.paytools.ali.UnifiedOrderingTool;
import io.swagger.annotations.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 进行页面支付
 */
@RestController
@RequestMapping(value = "/pagepay")
@Api(tags = "支付宝支付模块（页面进行支付）", description = "（PagePayController）")
public class PagePayController {
    private final Logger logger = LoggerFactory.getLogger(PagePayController.class);

    @RequestMapping(value = "/pageOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "支付宝的统一支付接口(app)", httpMethod = "POST", notes = "app", response = ResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "失败"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderno", value = "订单号", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "支付的金额,前段把金额进行判断只准数据两位小数", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "此单交易支付说明", paramType = "query"),
            @ApiImplicitParam(name = "url", value = "支付宝的异步处理接口", paramType = "query"),
    })
    public ResponseData appAlipay(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String orderno = request.getParameter("orderno");
        String amount = request.getParameter("amount");
        String title = request.getParameter("title");
        String url = request.getParameter("url");
        logger.info("接受的参数：金额：" + amount + ",订单：" + orderno + ",标题：" + title + ",回调地址：" + url);
        if (amount != null && amount != "" && title != null && title != "" && orderno != null && orderno != "" && url != null && url != "") {
            String alidata = UnifiedOrderingTool.payPageCode(amount, title, orderno, url);
            responseData.setCode(200);
            responseData.setData(alidata);
            responseData.setMessage("传输参数成功");
            return responseData;
        }
        logger.error("app支付传入的参数为空");
        responseData.setCode(500);
        return responseData;
    }
}
