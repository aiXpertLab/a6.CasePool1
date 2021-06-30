package com.seeingvoice.case37audiotrackpcm;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LeoReny@hypech.com on 2021/2/2.
 */

public class StreamThread extends Thread {

    private static final String TAG = "PlayThread";
    //private int mSampleRateInHz = 16000;
    private int mSampleRateInHz = 44100;
    // 单声道
    private int mChannelConfig = AudioFormat.CHANNEL_OUT_MONO;
    // private int mChannelConfig = AudioFormat.CHANNEL_OUT_STEREO;

    private Activity    oActivity;
    private AudioTrack  oAudioTrack;
    private byte[] aData;
    private String sFileName;

    public StreamThread(Activity activity, String fileName) {
        oActivity = activity;
        sFileName = fileName;

        int bufferSize = AudioTrack.getMinBufferSize(mSampleRateInHz, mChannelConfig, AudioFormat.ENCODING_PCM_16BIT);
        oAudioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                mSampleRateInHz,
                mChannelConfig,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM);
        if (null != oAudioTrack) {
            this.setChannel(true,false);
            oAudioTrack.play();
        }
    }

    @Override
    public void run() {
        super.run();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            InputStream inputStream = oActivity.getResources().getAssets().open(sFileName);

            // 缓冲区
            byte[] buffer = new byte[1024];
            // 播放进度
            int playIndex = 0;
            // 是否缓冲完成
            boolean isLoaded = false;
            // 缓冲 + 播放
            while (null != oAudioTrack && AudioTrack.PLAYSTATE_STOPPED != oAudioTrack.getPlayState()) {
                // 字符长度
                int len;
                if (-1 != (len = inputStream.read(buffer))) {
                    byteArrayOutputStream.write(buffer, 0, len);
                    aData = byteArrayOutputStream.toByteArray();
                    Log.e(TAG, "run: 已缓冲 : " + aData.length);
                } else {
                    // 缓冲完成
                    isLoaded = true;
                }

                if (AudioTrack.PLAYSTATE_PLAYING == oAudioTrack.getPlayState()) {
                    Log.e(TAG, "run: 开始从 " + playIndex + " 播放");
                    playIndex += oAudioTrack.write(aData, playIndex, aData.length - playIndex);
                    Log.e(TAG, "run: 播放到了 : " + playIndex);
                    if (isLoaded && playIndex == aData.length) {
                        Log.e(TAG, "run: 播放完了");
                        oAudioTrack.stop();
                    }

                    if (playIndex < 0) {
                        Log.e(TAG, "run: 播放出错");
                        oAudioTrack.stop();
                        break;
                    }
                }
            }
            Log.e(TAG, "run: play end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置左右声道是否可用
     *
     * @param left  左声道
     * @param right 右声道
     */
    public void setChannel(boolean left, boolean right) {
        if (null != oAudioTrack) {
            oAudioTrack.setStereoVolume(left ? 1 : 0, right ? 1 : 0);
            oAudioTrack.play();
        }
    }

    public void play() {
        if (null != oAudioTrack)
            oAudioTrack.play();
    }

    public void stopStream() {
        releaseAudioTrack();
    }

    private void releaseAudioTrack() {
        if (null != oAudioTrack) {
            oAudioTrack.stop();
            oAudioTrack.release();
            oAudioTrack = null;
        }
    }
}
