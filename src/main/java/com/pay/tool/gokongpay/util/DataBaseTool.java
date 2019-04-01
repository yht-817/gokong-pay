package com.pay.tool.gokongpay.util;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;

import java.util.Date;
import java.util.UUID;

/**
 * 说明:数据库工具类
 *
 * @author Mick
 * CreateDate 2018/6/9/009 21:53
 * Email ：ideacoding@163.com
 * Version 1.0
 **/
public class DataBaseTool {

    /**
     * 创建数据库ID (32位)
     *
     * @return
     */
    public static String createId() {
        return DigestUtil.md5Hex(UUID.randomUUID().toString());
    }

    /**
     * 创建数据库编码（参数+14位时间+4位随机）
     *
     * @param head 编码头
     * @return
     */
    public static String createNo(String head) {
        return head + DateUtil.currentSeconds() + RandomUtil.randomNumbers(8);
    }

    /**
     * 创建数据库时间
     *
     * @return
     */
    public static Date createDate() {
        return new Date();
    }

    /**
     * 创建用户昵称
     *
     * @return
     */
    public static String createNickName() {
        return "GoKong"+RandomUtil.randomNumbers(6);
    }
}
