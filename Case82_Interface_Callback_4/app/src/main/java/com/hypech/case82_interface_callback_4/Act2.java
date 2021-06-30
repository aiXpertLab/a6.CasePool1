package com.hypech.case82_interface_callback_4;

import android.content.Context;
import android.content.Intent;

public class Act2 {

    public RecognizerSV(Context context) {
        String s = "from Act2";
        mRecognizer = MLAsrRecognizer.createAsrRecognizer(context);   //1 用户调用接口创建一个语音识别器。
        mRecognizer.setAsrListener(new ListenerSV());   //2 绑定个listener
        Intent mRecoIntent = new Intent(MLAsrConstants.ACTION_HMS_ASR_SPEECH);
        mRecoIntent
                .putExtra(MLAsrConstants.LANGUAGE, "zh-CN")
                .putExtra(MLAsrConstants.FEATURE, MLAsrConstants.FEATURE_WORDFLUX);
        mRecognizer.startRecognizing(mRecoIntent);
    }
}
