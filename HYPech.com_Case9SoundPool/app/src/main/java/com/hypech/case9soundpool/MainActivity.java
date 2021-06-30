package com.hypech.case9soundpool;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

//public class MainActivity extends ActionBarActivity {

public class MainActivity extends AppCompatActivity {
    //本例適用於智小音樂，可調節聲音大小及播放時間(只是拉長波型)
    // 控制左右聲道用的SeekBar元件
    private SeekBar volume_left, volume_right;
    // 左右聲道音量，0.0F ~ 1.0F
    private float leftVolume = 1.0F, rightVolume = 1.0F;
    //存放音樂的Pool
    private SoundPool soundPool;
    private int soundId01, soundId02;
    //現在或最近一次播放的soudPool音效ID
    private int playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //獲取元件
        volume_left = (SeekBar) findViewById(R.id.volume_left);
        volume_right = (SeekBar) findViewById(R.id.volume_right);

        // 建立控制左右聲道的進度改變監聽物件
        SeekBar.OnSeekBarChangeListener listener =
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        switch (seekBar.getId()){
                            // 改變左聲道音量
                            case R.id.volume_left :
                                leftVolume = progress/10.0F;
                                break;
                            // 改變右聲道音量
                            case R.id.volume_right :
                                rightVolume = progress/10.0F;
                                break;
                        }
                        // 設定指定編號的左右聲道音量
                        soundPool.setVolume(playing,leftVolume,rightVolume);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                };
        //設置SeekBar的Listener
        volume_left.setOnSeekBarChangeListener(listener);
        volume_right.setOnSeekBarChangeListener(listener);

        // 建立SoundPool物件
        // 第一個參數設定音效最大數量
        // 第二個參數設定音效種類，通常指定為AudioManager.STREAM_MUSIC
        // 第三個參數設定播放音質，目前沒有作用
        // 載入指定資源編號的音樂並取得編號，第三個參數目前沒有作用
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .build();
        soundId01 = soundPool.load(this,R.raw.left,0);
        soundId02 = soundPool.load(this,R.raw.right,0);
    }

    //以下按下播放事件用activity_main.xml綁定在元件的click事件上
    public void clickPlay01(View view) {
        // 播放第一個音效
        soundPool.play(soundId01,leftVolume,rightVolume,0,0,1.0F);
        playing = soundId01;

    }

    public void clickPlay02(View view) {
        // 播放第二個音效
        soundPool.play(soundId02,leftVolume,rightVolume,0,0,1.0F);
        playing = soundId02;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
