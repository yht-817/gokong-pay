package com.pay.tool.gokongpay.web;

import com.pay.tool.gokongpay.model.ResponseData;
import com.pay.tool.gokongpay.paytools.ali.UnifiedOrderingTool;
import io.swagger.annotations.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 支付宝的公共接口
 * 支持远程调度
 */
@RestController
@RequestMapping(value = "/alipay")
@Api(tags = "支付宝支付模块", description = "（支付宝支付模块）")
public class AliPayController {
    private final Logger logger = LoggerFactory.getLogger(AliPayController.class);

    /**
     * （APP）支付宝的统一支付接口
     * 金额
     * 交易说明
     * 订单号
     * 异步通知
     */
    @RequestMapping(value = "/appunifiedorder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
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
            Map<String, String> alidata = UnifiedOrderingTool.doUnifiedOrderApp(amount, title, orderno, url);
            responseData.setCode(200);
            responseData.setData(alidata);
            responseData.setMessage("传输参数成功");
            return responseData;
        }
        logger.error("app支付传入的参数为空");
        responseData.setCode(500);
        return responseData;
    }

    /**
     * (WEB) 支付宝统一下单支付接口
     * <p>
     * 金额
     * 交易说明
     * 订单号
     * 异步通
     * 网页支付成功跳转的页面
     *
     * @return
     */
    @RequestMapping(value = "/webunifiedorder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "支付宝的统一支付接口(web)", httpMethod = "POST", notes = "web", response = ResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "失败"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "支付的金额,前段把金额进行判断只准数据两位小数", paramType = "query"),
            @ApiImplicitParam(name = "orderno", value = "订单号", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "此单交易支付说明", paramType = "query"),
            @ApiImplicitParam(name = "weburl", value = "web支付成功后跳转的页面", paramType = "query"),
            @ApiImplicitParam(name = "url", value = "支付宝的异步处理接口", paramType = "query"),
    })
    public ResponseData webAlipay(HttpServletRequest request, HttpServletResponse response) {
        String orderno = request.getParameter("orderno");
        String amount = request.getParameter("amount");
        String weburl = request.getParameter("weburl");
        String title = request.getParameter("title");
        String url = request.getParameter("url");
        ResponseData responseData = new ResponseData();
        if (amount != null && amount != "" && title != null && title != "" && orderno != null && orderno != "" && url != null && url != "" && weburl != null && weburl != "") {
            Map<String, String> alidata = UnifiedOrderingTool.doUnifiedOrderWeb(amount, title, orderno, url, weburl);
            responseData.setCode(200);
            responseData.setData(alidata);
            return responseData;
        }
        logger.error("web支付传入的参数为空");
        responseData.setCode(500);
        return responseData;
    }

    /**
     * 支付查询接口
     *
     * @param orderno 支付宝订单和商户的订单号
     * @return
     */
    @RequestMapping(value = "/queryorder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "支付查询是否成功查询接口", httpMethod = "POST", notes = "查询", response = ResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "失败"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderno", value = "支付宝订单号或商户订单号", paramType = "query"),
    })
    public ResponseData webAlipay(@RequestParam String orderno) {
        ResponseData responseData = new ResponseData();
        if (orderno != null && orderno != "") {
            String alidata = UnifiedOrderingTool.queryAliPay(orderno);
            responseData.setCode(200);
            responseData.setData(alidata);
            return responseData;
        }
        logger.error("web支付传入的参数为空");
        responseData.setCode(500);
        return responseData;
    }

    /**
     * 支付退款接口(支付宝)
     */
    @RequestMapping(value = "/refundalipay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "退款是否成功", httpMethod = "POST", notes = "退款", response = ResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "失败"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderno", value = "支付宝订单号或商户订单号", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "退款的金额", paramType = "query"),
    })
    public ResponseData refundAliPay(HttpServletRequest request, HttpServletResponse response) {
        String orderno = request.getParameter("orderno");
        String amount = request.getParameter("amount");
        ResponseData responseData = new ResponseData();
        if (orderno != null && orderno != "" && amount != null && amount != "") {
            String alidata = UnifiedOrderingTool.refundAliPay(orderno, amount);
            responseData.setCode(200);
            responseData.setData(alidata);
            return responseData;
        }
        logger.error("退款的参数为空");
        responseData.setCode(500);
        return responseData;
    }

    /**
     * 交易取消
     *
     * @param orderno 支付宝订单和商户的订单号
     * @return
     */
    @RequestMapping(value = "/closealipay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "交易取消", httpMethod = "POST", notes = "取消", response = ResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "失败"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderno", value = "支付宝订单号或商户订单号", paramType = "query"),
    })
    public ResponseData closeAliPay(@RequestParam String orderno) {
        ResponseData responseData = new ResponseData();
        if (orderno != null && orderno != "") {
            String alidata = UnifiedOrderingTool.closeAliPay(orderno);
            responseData.setCode(200);
            responseData.setData(alidata);
            return responseData;
        }
        logger.error("退款的参数为空");
        responseData.setCode(500);
        return responseData;
    }
}
