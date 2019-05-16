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
    /**
     * @param context 上下文
     * @param start 起点
     * @param end 终点
     * @param type 测量方式  直线或者驾车
     * */
    public static String getDistance(Context context,LatLonPoint start, LatLonPoint end,int type){
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
            }
        });
        search.calculateRouteDistanceAsyn(query);
        return "";
    }
}
