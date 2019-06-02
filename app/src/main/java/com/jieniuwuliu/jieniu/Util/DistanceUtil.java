package com.jieniuwuliu.jieniu.Util;

import android.content.Context;
import android.util.Log;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * 计算距离
 * */
public class DistanceUtil {
    private static final double EARTH_RADIUS = 6378.137;
    private static double distance = 0.0;

    private static double rad(double d){
        return d * Math.PI / 180.0;
    }
    /**
     * @param context 上下文
     * @param start 起点
     * @param end 终点
     * @param type 测量方式  直线或者驾车
     * */
    public static double getDistance(Context context,LatLonPoint start, LatLonPoint end,int type){
        List<LatLonPoint> list = new ArrayList<>();
        list.add(start);
        DistanceSearch search = new DistanceSearch(context);
        DistanceSearch.DistanceQuery query = new DistanceSearch.DistanceQuery();
        query.setOrigins(list);//支持多起点
        query.setDestination(end);
        //设置测量方式，支持直线和驾车
        query.setType(type);//设置为驾车
        search.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
            @Override
            public void onDistanceSearched(DistanceResult distanceResult, int i) {
                Log.w("distance",distanceResult.toString());
                distance = Double.valueOf(distanceResult.getDistanceResults().get(0).getDistance());
            }
        });
        search.calculateRouteDistanceAsyn(query);
        return distance;
    }
    public static double Distance(double nowLng, double nowLat, double latitude, double longitude) {
        double long1;
        double lat1;
        double long2;
        double lat2;
        double a, b, sa2, sb2,d = 0;
        try {
            long1 = nowLng;
            lat1 = nowLat;
            long2 = latitude;
            lat2 = longitude;
            lat1 = rad(lat1);
            lat2 = rad(lat2);
            a = lat1 - lat2;
            b = rad(long1 - long2);
            sa2 = Math.sin(a / 2.0);
            sb2 = Math.sin(b / 2.0);
            d = 2   * EARTH_RADIUS
                    * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                    * Math.cos(lat2) * sb2 * sb2));
        } catch (Exception e) {

            e.printStackTrace();
        }
        return d;
    }
}
