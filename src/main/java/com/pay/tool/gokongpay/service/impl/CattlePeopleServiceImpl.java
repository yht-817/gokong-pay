package com.pay.tool.gokongpay.service.impl;

import com.pay.tool.gokongpay.dao.CattlePeopleDao;
import com.pay.tool.gokongpay.service.CattlePeopleService;
import com.pay.tool.gokongpay.util.DataBaseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class CattlePeopleServiceImpl implements CattlePeopleService, Serializable {

    @Autowired
    CattlePeopleDao cattlePeopleDao;

    /**
     * 插入牛人支付的记录表
     *
     * @param id      ID
     * @param orderno 订单编码
     * @param type    支付方式
     * @param amount  支付金额
     * @param userno
     * @return
     */
    public int addPeoplePay(String id, String orderno, String type, BigDecimal amount, String userno) {
        return cattlePeopleDao.addPeoplePayLog(id, orderno, type, amount, userno, DataBaseTool.createDate());
    }


    /**
     *  查询当前的方式和金额
     * @param orderno
     * @return
     */
    public Map<String, Object> queryPayInfo(String orderno) {
        return cattlePeopleDao.queryPayInfo(orderno);
    }
}
