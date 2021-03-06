package com.pay.tool.gokongpay.web;

import cn.hutool.log.StaticLog;
import com.pay.tool.gokongpay.model.ResponseData;
import com.pay.tool.gokongpay.paytools.ali.UnifiedOrderingTool;
import com.pay.tool.gokongpay.paytools.wx.WXPay;
import com.pay.tool.gokongpay.paytools.wx.WXPayConfigImpl;
import com.pay.tool.gokongpay.paytools.wx.WXPayUtil;
import com.pay.tool.gokongpay.service.CattlePeopleService;
import com.pay.tool.gokongpay.util.DataBaseTool;
import io.swagger.annotations.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 牛人支付
 */
@RestController
@RequestMapping(value = "/v2/peoplePay")
@Api(tags = "牛人支付模块", description = "（牛人支付模块）")
public class CattlePeopleController {
    @Autowired
    private CattlePeopleService cattlePeopleService;

    private static WXPay wxpay;
    private static WXPayConfigImpl config;

    static {
        try {
            config = WXPayConfigImpl.getInstance();
            wxpay = new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 牛人的支付
     */
    @RequestMapping(value = "/peoplePayOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "支付宝的统一支付接口(app)", httpMethod = "POST", notes = "app", response = ResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "失败"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "支付人的用户编码", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "支付的金额,前段把金额进行判断只准数据两位小数", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "此单交易支付说明", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "支付类型(微信2|支付宝1)", paramType = "query"),
    })
    public ResponseData payParameters(@RequestParam(name = "userNo", required = false) String userNo, @RequestParam(name = "amount", required = false) String amount,
                                      @RequestParam(name = "title", required = false) String title, @RequestParam(name = "type", required = false) String type) {
        ResponseData responseData = new ResponseData();
        String orderNo = DataBaseTool.createNo("JHY");
        StaticLog.info("接受的参数：金额：" + amount + ",标题：" + title + ",用户编码：" + userNo + ",订单编码：" + orderNo + ":类型：" + type);
        if (amount != null && amount != "" && title != null && title != "" && orderNo != null && orderNo != "") {
            if (type.equals("1")) {
                type = "支付宝";
            } else {
                type = "微信";
            }
            // 插入数据支付日志
            int add_Pay_Log = cattlePeopleService.addPeoplePay(DataBaseTool.createId(), orderNo, type, BigDecimal.valueOf(Double.valueOf(amount)), userNo);
            if (add_Pay_Log > 0) {
                if (type.equals("支付宝")) {
                    String url = "http://47.98.139.112:8080/v2/asyn/peopelPayAli";
                    Map<String, String> alidata = UnifiedOrderingTool.doUnifiedOrderApp(amount, title, orderNo, url);
                    responseData.setCode(200);
                    responseData.setData(alidata.get("sdk_data"));
                    responseData.setMessage("传输参数成功");
                    return responseData;
                } else {
                    String url = "http://47.98.139.112:8080/v2/asyn/peopelPayWx";
                    Map<String, String> resultdata = null;
                    try {
                        Map<String, String> datamap = new HashMap<>();
                        datamap.put("body", title);
                        datamap.put("out_trade_no", orderNo);
                        datamap.put("total_fee", WXPayUtil.yuanToSubdivision(amount));
                        datamap.put("spbill_create_ip", WXPayUtil.getIP());
                        datamap.put("trade_type", "APP");
                        datamap.put("notify_url", url);
                        resultdata = wxpay.unifiedOrder(datamap);
                        if (resultdata != null) {
                            // 进行再次签名
                            Map<String, String> datas = new HashMap<>();
                            datas.put("prepayid", resultdata.get("prepay_id"));
                            // 再次签名
                            Map<String, String> againmap = wxpay.againOrder(datas);
                            if (againmap != null) {
                                StaticLog.info("微信返回的结果：" + resultdata.toString());
                                responseData.setData(againmap);
                                responseData.setCode(200);
                                return responseData;
                            } else {
                                responseData.setCode(500);
                                responseData.setMessage("第二次签名的数据为空");
                                return responseData;
                            }
                        } else {
                            responseData.setMessage("第一次签名失败");
                            responseData.setCode(500);
                            return responseData;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        StaticLog.error("微信APP支付的错误：" + e.getMessage());
                        responseData.setCode(500);
                        return responseData;
                    }
                }
            } else {
                responseData.setCode(500);
                responseData.setMessage("支付失败");
                return responseData;
            }
        } else {
            responseData.setCode(500);
            responseData.setMessage("支付参数为空");
            return responseData;
        }
    }

    /**
     * 退款接口
     */
    @RequestMapping(value = "/peopleRefund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "牛人订单退款", httpMethod = "POST", notes = "退款", response = ResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "失败"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "支付宝订单号或商户订单号", paramType = "query"),
    })
    public ResponseData refundAliPay(HttpServletRequest request) {
        String orderNo = request.getParameter("orderNo");
        StaticLog.info("订单编码：{}" + orderNo);
        ResponseData responseData = new ResponseData();
        Map<String, Object> dataPay = cattlePeopleService.queryPayInfo(orderNo);
        if (dataPay != null) {
            String amount = String.valueOf(dataPay.get("pay_amount"));
            String payType = (String) dataPay.get("pay_mode_no");
            if (payType.equals("支付宝")) {
                StaticLog.info("当前为支付宝退款信息");
                String alidata = UnifiedOrderingTool.refundAliPay(orderNo, amount);
                StaticLog.error("退款的结果:" + alidata);
                responseData.setCode(200);
                responseData.setData(alidata);
                return responseData;
            } else {
                StaticLog.info("当前为微信退款信息:" + WXPayUtil.yuanToSubdivision(amount) + "分");
                try {
                    Map<String, String> datamap = new HashMap<>();
                    datamap.put("refund_desc", "牛人拒绝好友申请");
                    datamap.put("out_trade_no", orderNo);
                    datamap.put("out_refund_no", DataBaseTool.createNo("GOTK"));
                    datamap.put("total_fee", WXPayUtil.yuanToSubdivision(amount));
                    datamap.put("refund_fee", WXPayUtil.yuanToSubdivision(amount));
                    Map<String, String> resultdata = wxpay.refund(datamap);
                    StaticLog.info("微信返回的参数:" + resultdata.toString());
                    if (resultdata.get("return_code").equals("SUCCESS")) {
                        StaticLog.info("微信退款成功");
                        responseData.setCode(200);
                        responseData.setData("微信返款成功");
                        return responseData;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        responseData.setCode(500);
        responseData.setData("参数不能为空");
        return responseData;
    }

}
