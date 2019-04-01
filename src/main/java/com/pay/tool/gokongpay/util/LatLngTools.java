package com.pay.tool.gokongpay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据经纬度获取两点的的距离
 */
public class LatLngTools {
    private final Logger logger = LoggerFactory.getLogger(LatLngTools.class);
    private static final double EARTH_RADIUS = 6378.137;//地球半径,单位千米

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 传入两点的经纬度
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 返回距离两点的距离（km）
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
