package com.hypech.case82_interface_callback;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

/*
比如你暗恋一个女孩很久了，然后有一天你给她递了小纸条，为了收到她看完小纸条后的想法，你在小纸条的结尾附加上
了你的电话号码并且告诉她，如果你也喜欢我就给我打电话吧告诉我“你愿意”（那么“打电话”就是你们约定好的接口）。
 */
public class MainActivity extends AppCompatActivity {

    private XiaoZhiTiao mXiaoZhiTiao;//小纸条（里面有打电话这件事）, interface variable，指向interface reference.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mXiaoZhiTiao = (XiaoZhiTiao)new MyGirl();//小纸条要给谁（这里给的是我的妹子）
        // 这个new是实现接口的类。interface variable通过这句话，reference从interface变成class/object
        myGirlLook();
    }

    private void myGirlLook(){
        for (int i=0; i<10; i++){
            Log.e("girl is looking the xiaozhitiao ", ""+i);
        }
        mXiaoZhiTiao.call("I do!!!");
    }
}