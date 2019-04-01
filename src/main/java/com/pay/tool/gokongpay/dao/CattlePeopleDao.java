package com.pay.tool.gokongpay.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface CattlePeopleDao {

    @Insert("INSERT INTO user_pay_detail(id,user_no,pay_no,pay_mode_no,pay_amount,pay_date)VALUES(#{id},#{userno},#{orderno},#{type},#{amount},#{date})")
    int addPeoplePayLog(@Param("id") String id, @Param("orderno") String orderno, @Param("type") String type, @Param("amount") BigDecimal amount, @Param("userno") String userno, @Param("date") Date date);

    @Select("SELECT pay_amount,pay_mode_no FROM user_pay_detail WHERE pay_no = #{orderno}")
    Map<String, Object> queryPayInfo(@Param("orderno") String orderno);
}
