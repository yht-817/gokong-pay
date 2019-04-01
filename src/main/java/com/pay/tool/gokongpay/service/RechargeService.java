package com.pay.tool.gokongpay.service;

public interface RechargeService {
    boolean addRecharge(String orderno, String userNo, String amount);
}
