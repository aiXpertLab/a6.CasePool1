package com.hypech.case35_at_static_pcm;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.hypech.case35_at_static_pcm.GlobalConfig.SAMPLE_RATE;


public class PlayThread extends Thread {
    private Activity   oActivity;
    private AudioTrack oAudioTrack;
    private byte[] aAudioData;
    private int    vRawId;

    public PlayThread(){}

    public PlayThread(Activity activity, int rawid) {
        oActivity = activity;
        vRawId   = rawid;
    }

    @Override
    public void run() {
        super.run();
        try {
            try (InputStream in = oActivity.getResources().openRawResource(vRawId)) {

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                for (int b; (b = in.read()) != -1; ) {
                    out.write(b);
                }
                aAudioData = out.toByteArray();
            }
        } catch (IOException e) {
            Log.wtf("TAG", "Failed to read", e);
        }

        oAudioTrack = new AudioTrack(
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                new AudioFormat.Builder().setSampleRate(SAMPLE_RATE)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build(),
                aAudioData.length,
                AudioTrack.MODE_STATIC,
                AudioManager.AUDIO_SESSION_ID_GENERATE);
        oAudioTrack.write(aAudioData, 0, aAudioData.length);
        oAudioTrack.play();
    }

    public void stopPlay() {
        if (null != oAudioTrack) {
            oAudioTrack.stop();
            oAudioTrack.release();
            oAudioTrack = null;
        }
    }

}