package com.reapex.sv.utils;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.reapex.sv.entitydaodb.Area;
import com.reapex.sv.entitydaodb.Region;

import java.util.List;

/**
 * 地区工具类
 *
 * @author zhou
 */
public class RegionUtil {

    /**
     * 初始化地区信息
     */
    public static void initRegion(Context context) {
        if (Region.count(Region.class) == 0) {
            initData(context);
        }
    }

    private static void initData(Context context) {
        // 获取assets目录下的json文件数据
        String jsonData = new GetJsonDataUtil().getJson(context, "region-wx.json");
        List<Region> regionList = JSONArray.parseArray(jsonData, Region.class);
        Area.saveInTx(regionList);
    }
}
