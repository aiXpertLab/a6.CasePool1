package com.hypech.case82_interface_callback;

import android.util.Log;

public class MyGirl implements XiaoZhiTiao{
    //实现接口，实现接口方法
    @Override
    public void call(String msg) {
        Log.e("girl saying: ", msg);
    }
}
