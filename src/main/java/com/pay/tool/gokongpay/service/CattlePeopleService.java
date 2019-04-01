package com.pay.tool.gokongpay.service;

import java.math.BigDecimal;
import java.util.Map;

public interface CattlePeopleService {
    int addPeoplePay(String id, String orderno, String type, BigDecimal amount, String userno);

    Map<String, Object> queryPayInfo(String orderno);
}
