package com.hypech.audiotrackpcm;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private PlayThread mPlayThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play =  findViewById(R.id.bt_play);
        Button stop =  findViewById(R.id.bt_stop);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);

        Button channelLeft =  findViewById(R.id.channel_left);
        Button channelRight = findViewById(R.id.channel_right);
        channelLeft.setOnClickListener(this);
        channelRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_play: // 开始播放
                if (null != mPlayThread) {
                    mPlayThread.stopp();
                    mPlayThread = null;
                }
                mPlayThread = new PlayThread(this, "tts1.pcm");
                mPlayThread.start();
                break;
            case R.id.bt_stop: // 停止
                if (null != mPlayThread) {
                    mPlayThread.stopp();
                    mPlayThread = null;
                }
                break;
            case R.id.channel_left: // 禁用左声道
                if (null != mPlayThread)
                    mPlayThread.setChannel(false, true);
                // mBalance.setProgress(0);
                break;
            case R.id.channel_right: // 禁用右声道
                if (null != mPlayThread)
                    mPlayThread.setChannel(true, false);
                // mBalance.setProgress(mBalance.getMax());
                break;
            default:
                break;
        }
    }
}
