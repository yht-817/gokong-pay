package com.pay.tool.gokongpay.domain;


import java.io.Serializable;

/**
 * 支付参数
 */
public class PayParameter implements Serializable {
    
    private String userNo;
    private String amount;
    private String title;
    private String type;
    private String url;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PayParameter{" +
                "userNo='" + userNo + '\'' +
                ", amount='" + amount + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
