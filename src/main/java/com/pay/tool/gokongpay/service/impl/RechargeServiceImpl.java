package com.pay.tool.gokongpay.service.impl;

import com.pay.tool.gokongpay.dao.RechargeDao;
import com.pay.tool.gokongpay.service.RechargeService;
import com.pay.tool.gokongpay.util.DataBaseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;


@Service
public class RechargeServiceImpl implements RechargeService, Serializable {
    @Autowired
    RechargeDao rechargeDao;

    @Override
    public boolean addRecharge(String orderno, String userNo, String amount) {
        String rechargeType = "微信小程序";
        int add = rechargeDao.addRecharge(DataBaseTool.createId(), orderno, userNo, amount, rechargeType, new Date());
        if (add > 0) {
            return true;
        }
        return false;
    }
}
