package com.hypech.case82_interface_callback2;

import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SubCallee {

    private final String IMGURL = "http://f.hiphotos.baidu.com/zhidao/pic/item/b21bb051f8198618a323ac464bed2e738ad4e688.jpg";
    //持有接口变量
    LeoInterface mInterface;

    SubCallee(LeoInterface theInterface) {
        //这里是关键，把外部的接口实例引用到该类，给变量赋值
        mInterface = theInterface;
        //创建对象的时候执行下载
        executeDown();
    }
    //在这里我们声明了一个接口变量，在类的初始化方法中把接口的子类的地址赋给该变量并且调用下载数据的方法，
    // 在数据下载完成之后调用接口变量的方法把数据传给该方法，执行该方法实际是执行子类的该方法，这就是接口回调真正做的事。

    public void executeDown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int i =1;
                    HttpURLConnection conn = (HttpURLConnection) new URL(IMGURL).openConnection();
                    conn.setConnectTimeout(5 * 1000);
                    conn.setRequestMethod("GET");
                    mInterface.result(BitmapFactory.decodeStream(conn.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
