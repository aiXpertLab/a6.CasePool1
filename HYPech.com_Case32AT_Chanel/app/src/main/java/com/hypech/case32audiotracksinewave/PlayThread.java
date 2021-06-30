package com.hypech.case32audiotracksinewave;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by LeoReny@hypech.com on 2021/2/2.
 */

public class PlayThread extends Thread {
    //人耳能够感觉到的最高频率为20kaudioFrequency，因此要满足人耳的听觉要求，则需要至少每秒进行40k次采样，
    // 这个40kHz就是采样率。我们常见的CD音质，采样率为44.1kaudioFrequency。
    public static final int CD_SAMPLE_RATE = 44100;

    AudioTrack mAudioTrack;
    public static boolean ISPLAYSOUND;

    int length,  numOfSamplesPerWave,  audioFrequency;
    byte[] wave;

    /**
     * 初始化
     * @param rate 频率
     */
    public PlayThread(int rate) {
        if (rate > 0) {
            audioFrequency = rate;                      //sound actual frequency
            numOfSamplesPerWave = CD_SAMPLE_RATE / audioFrequency;  //number of sample points per full wave
            length = numOfSamplesPerWave * audioFrequency;
            wave = new byte[CD_SAMPLE_RATE];
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, CD_SAMPLE_RATE,
                    AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_8BIT, length, AudioTrack.MODE_STREAM);
            ISPLAYSOUND = true;
            wave = SinWave.sin(wave, numOfSamplesPerWave, length);
        }
    }

    @Override
    public void run() {
        super.run();
        if (null != mAudioTrack)
            mAudioTrack.play();
        //一直播放
        while (ISPLAYSOUND) {
            mAudioTrack.write(wave, 0, length);
        }

    }

    /**
     * 设置左右声道，左声道时设置右声道音量为0，右声道设置左声道音量为0
     *
     * @param left  左声道
     * @param right 右声道
     */
    public void setChannel(boolean left, boolean right) {
        if (null != mAudioTrack) {
            mAudioTrack.setStereoVolume(left ? 1 : 0, right ? 1 : 0);
        }
    }

    //设置音量
    public void setVolume(float left, float right) {
        if (null != mAudioTrack) {
            mAudioTrack.setStereoVolume(left,right);
        }
    }

    public void stopPlay() {
        ISPLAYSOUND = false;
        releaseAudioTrack();
    }

    private void releaseAudioTrack() {
        if (null != mAudioTrack) {
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }
}
