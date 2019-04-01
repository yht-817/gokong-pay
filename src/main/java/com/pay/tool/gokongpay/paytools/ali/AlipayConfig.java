package com.pay.tool.gokongpay.paytools.ali;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * 支付宝进行支付参数
 */
public class AlipayConfig {
    private static final Logger log = LoggerFactory.getLogger(AlipayConfig.class);
    /**
     * 网关地址
     */
    public static String URL;

    /**
     * APPID
     */
    public static String KEY_ID;

    /**
     * 私钥
     */
    public static String PRIVATE_KEY;

    /**
     * 支付宝公钥
     */
    public static String PUBLIC_KEY;

    /**
     * 签名算法类型(根据生成私钥的算法,RSA2或RSA)
     */
    public static String SIGNTYPE;

    /**
     * 请求数据格式
     */
    public static String FORMAT;

    /**
     * 编码集
     */
    public static String CHARSET;

    /**
     * 支付宝客户端
     */
    private static AlipayClient alipayClient = null;


    /**
     * 从配置文件里面读取信息
     */
    static {
        InputStream inputStream = AlipayConfig.class.getClassLoader().getResourceAsStream("alipay.properties");
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        URL = prop.getProperty("access.url");       // 网关请求地址
        KEY_ID = prop.getProperty("access.key.id"); // 用户ID
        FORMAT = prop.getProperty("access.format");      // 数据请求格式
        CHARSET = prop.getProperty("access.charset");    // 数据集编码格式
        SIGNTYPE = prop.getProperty("access.signtype");  // 签名算法
        PUBLIC_KEY = prop.getProperty("access.public.key");   // 公钥
        PRIVATE_KEY = prop.getProperty("access.private.key"); // 私钥
    }

    /**
     * 获得初始化的AlipayClient
     * 链接客户端
     */
    public static AlipayClient getAlipayClient() {
        if (alipayClient == null) {
            synchronized (AlipayConfig.class) {
                if (null == alipayClient) {
                    alipayClient = new DefaultAlipayClient(URL, KEY_ID, PRIVATE_KEY, FORMAT, CHARSET, PUBLIC_KEY, SIGNTYPE);
                }
            }
        }
        return alipayClient;
    }
}
