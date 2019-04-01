package com.pay.tool.gokongpay.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface RechargeDao {
    @Insert("INSERT INTO user_pay_detail VALUES (#{id}, #{userNo}, #{orderno}, #{rechargeType}, #{amount}, '0', #{date})")
    int addRecharge(@Param("id") String id, @Param("orderno") String orderno, @Param("userNo") String userNo, @Param("amount") String amount, @Param("rechargeType") String rechargeType, @Param("date") Date date);
}
