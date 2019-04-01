package com.pay.tool.gokongpay.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * 类说明:数据请求实体
 *
 * @author ideacoding
 * @create 2018-01-18 11:33
 * @Email ：ideacoding@163.com
 **/
@ApiModel(value = "RequestData",description = "数据请求实体")
public class RequestData<T> {
    //@JsonProperty(value = "OS")
    @ApiModelProperty(dataType = "String",name = "os",value = "客户端类型")
    @NotNull(message = "客户端类型不能为空")
    private String os;

    @ApiModelProperty(dataType = "String",name = "v",value = "版本")
    @NotNull(message = "版本不能为空")
    private String v;

    @ApiModelProperty(dataType = "T",name = "data",value = "变化参数")
    @Valid
    @NotNull(message = "业务参数不能为空")
    private T data;

    @ApiModelProperty(dataType = "String",name = "userNo",value = "用户编码")
    @NotNull(message = "用户编码不能为空")
    private String userNo;

    @ApiModelProperty(dataType = "String",name = "lang",value = "语言 zh=中文 en=英文")
    private String lang = "zh";

    @ApiModelProperty(dataType = "String",name = "token",value = "token")
    private String token;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RequestData(String os, String v, T data, String userNo, String lang) {
        this.os = os;
        this.v = v;
        this.data = data;
        this.userNo = userNo;
        this.lang = lang;
    }

    public RequestData() {
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "os='" + os + '\'' +
                ", v='" + v + '\'' +
                ", data=" + data +
                ", userNo='" + userNo + '\'' +
                ", lang='" + lang + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
