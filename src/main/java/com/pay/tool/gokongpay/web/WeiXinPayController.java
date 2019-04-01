package com.pay.tool.gokongpay.web;

import com.pay.tool.gokongpay.model.ResponseData;
import com.pay.tool.gokongpay.paytools.wx.WXPay;
import com.pay.tool.gokongpay.paytools.wx.WXPayConfigImpl;
import com.pay.tool.gokongpay.paytools.wx.WXPayUtil;
import com.pay.tool.gokongpay.service.CattlePeopleService;
import com.pay.tool.gokongpay.service.RechargeService;
import com.pay.tool.gokongpay.util.DataBaseTool;
import io.swagger.annotations.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付的公共接口
 * 提供远程调度
 */
@RestController
@RequestMapping(value = "/wxpay")
@Api(tags = "微信支付模块", description = "（微信支付）")
public class WeiXinPayController {
    private final Logger logger = LoggerFactory.getLogger(WeiXinPayController.class);

    @Autowired
    private RechargeService rechargeService;

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
     * （APP支付）微信支付统一下发接口
     */
    @RequestMapping(value = "/appunifiedorder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "支付宝的统一支付接口(app)", httpMethod = "POST", notes = "app", response = ResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "失败"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "支付的金额,前段把金额进行判断只准数据两位小数", paramType = "query"),
            @ApiImplicitParam(name = "orderno", value = "订单号", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "此单交易支付说明", paramType = "query"),
            @ApiImplicitParam(name = "url", value = "支付宝的异步处理接口", paramType = "query"),
    })
    public ResponseData appAlipay(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String orderno = request.getParameter("orderno");
        String amount = request.getParameter("amount");
        String title = request.getParameter("title");
        String url = request.getParameter("url");
        if (amount != null && amount != "" && title != null && title != "" && orderno != null && orderno != "" && url != null && url != "") {
            Map<String, String> resultdata = null;
            try {
                Map<String, String> datamap = new HashMap<>();
                datamap.put("body", title);
                datamap.put("out_trade_no", orderno);
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
                        logger.info("微信返回的结果：" + resultdata.toString());
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
                logger.error("微信APP支付的错误：" + e.getMessage());
                responseData.setCode(500);
                return responseData;
            }
        }
        logger.error("app支付传入的参数为空");
        responseData.setCode(500);
        responseData.setMessage("传入的参数为空");
        return responseData;
    }

    /**
     * (WEB) 微信统一下单支付接口
     * <p>
     * 金额
     * 交易说明
     * 订单号
     * 异步通
     * 网页支付成功跳转的页面
     * 客户端真实IP
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
            @ApiImplicitParam(name = "ip", value = "客户端的IP", paramType = "query"),
    })
    public ResponseData webAlipay(HttpServletRequest request, HttpServletResponse response) {
        String orderno = request.getParameter("orderno");
        String amount = request.getParameter("amount");
        String weburl = request.getParameter("weburl");
        String title = request.getParameter("title");
        String url = request.getParameter("url");
        String ip = request.getParameter("ip");
        ResponseData responseData = new ResponseData();
        if (amount != null && amount != "" && title != null && title != "" && orderno != null && orderno != "" && url != null && url != "" && ip != null && ip != "") {
            Map<String, String> datamap = new HashMap<>();
            datamap.put("body", title);
            datamap.put("out_trade_no", orderno);
            datamap.put("total_fee", WXPayUtil.yuanToSubdivision(amount));
            datamap.put("spbill_create_ip", ip);
            datamap.put("trade_type", "MWEB");
            datamap.put("notify_url", url);
            datamap.put("scene_info", "{\"h5_info\" : {\"type\": \"Wap\",\"wap_url\": \"www.sunwukong.net/h5/components/Success.html\",\"wap_name\": \"孙悟空华人网\"}}");
            try {
                Map<String, String> resultdata = wxpay.unifiedOrder(datamap);
                logger.info(resultdata.toString());
                if (resultdata.get("return_code").equals("SUCCESS")) {
                    // 返回支付调起客户端的地址url再次签名
                    Map<String, String> datas = new HashMap<>();
                    datas.put("prepayid", resultdata.get("prepay_id"));
                    logger.error("支付的支付ID--：" + datas.toString());
                    // 进行再次签名
                    Map<String, String> webdata = wxpay.againOrder(datas);
                    logger.error("再次返回签名后的map集合：" + webdata.toString());
                    responseData.setCode(200);
                    responseData.setData(resultdata);
                    return responseData;
                } else {
                    logger.error("H5微信预支付失败");
                    responseData.setCode(500);
                    responseData.setMessage("H5微信预支付失败");
                    return responseData;
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseData.setCode(500);
                responseData.setMessage(e.getMessage());
                return responseData;
            }
        }
        logger.error("web支付传入的参数为空");
        responseData.setCode(500);
        responseData.setMessage("传入的参数为空");
        return responseData;
    }

    /**
     * 微信小程序支付
     */
    @RequestMapping(value = "/smallProgramPay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "微信小程序支付", httpMethod = "POST", response = ResponseData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "失败"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "支付的金额,前段把金额进行判断只准数据两位小数", paramType = "query"),
            @ApiImplicitParam(name = "userNo", value = "用户编码", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "此单交易支付说明", paramType = "query"),
            @ApiImplicitParam(name = "url", value = "支付宝的异步处理接口", paramType = "query"),
    })
    public ResponseData smallProgramPay(@RequestParam(name = "userNo") String userNo, @RequestParam(name = "amount") String amount,
                                        @RequestParam(name = "title") String title, @RequestParam(name = "url") String url, @RequestParam(name = "openid") String openid) throws Exception {
        ResponseData responseData = new ResponseData();
        if (amount != null && amount != "" && title != null && title != "" && userNo != null && userNo != "" && url != null && url != "") {
            String orderno = DataBaseTool.createNo("CZ");
            // 在用户支付记录表中进行日志记录
            boolean add = rechargeService.addRecharge(orderno, userNo, amount);
            if (add) {
                Map<String, String> resultdata = null;
                Map<String, String> datamap = new HashMap<>();
                datamap.put("body", title);
                datamap.put("out_trade_no", orderno);
                datamap.put("total_fee", WXPayUtil.yuanToSubdivision(amount));
                datamap.put("spbill_create_ip", WXPayUtil.getIP());
                datamap.put("trade_type", "JSAPI");
                datamap.put("openid", openid);
                datamap.put("notify_url", url);
                resultdata = wxpay.unifiedOrders(datamap);
                if (!resultdata.get("return_code").equals("FAIL")) {
                    Map<String, String> datas = new HashMap<>();
                    datas.put("prepayid", "prepay_id=" + resultdata.get("prepay_id"));
                    // 再次签名
                    Map<String, String> againmap = wxpay.againOrders(datas);
                    if (againmap != null) {
                        logger.info("微信返回的结果：" + resultdata.toString());
                        responseData.setData(againmap);
                        responseData.setCode(200);
                        return responseData;
                    }
                }
            }
        }
        responseData.setCode(500);
        responseData.setMessage("传入的参数为空");
        return responseData;
    }
}
