package com.hypech.case39volume;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private LinearLayout mRootLayout;
    private Button mBtnSetMediaVolume;
    private TextView mTVStats;

    private AudioManager mAudioManager;
    private Random mRandom = new Random();

    private int random_volume_sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        // Get the widget reference from xml layout
        mRootLayout = findViewById(R.id.root_layout);
        mBtnSetMediaVolume = findViewById(R.id.btn_media);
        mTVStats = findViewById(R.id.tv_stats);

        /*
            AudioManager
                AudioManager provides access to volume and ringer mode control.

                Instances of this class must be obtained using
                Context.getSystemService(Class) with the argument AudioManager.class or
                Context.getSystemService(String) with the argument Context.AUDIO_SERVICE.
        */

        // Get the audio manager instance
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Set click listener for media button
        mBtnSetMediaVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    STREAM_MUSIC
                        Used to identify the volume of audio streams for music playback
                */

                /*
                    int getStreamVolume (int streamType)
                        Returns the current volume index for a particular stream.

                    Parameters
                        streamType int : The stream type whose volume index is returned.

                    Returns
                        int : The current volume index for the stream.
                */
                int media_current_volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

                /*
                    int getStreamMaxVolume (int streamType)
                        Returns the maximum volume index for a particular stream.

                    Parameters
                        streamType int : The stream type whose maximum volume index is returned.

                    Returns
                        int : The maximum valid volume index for the stream.
                */
                int media_max_volume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                // Get a random number between a specified range
                //sv int random_volume = mRandom.nextInt(((media_max_volume - 0) + 1) + 0);
                random_volume_sv = random_volume_sv + 10;
                if (random_volume_sv >=100) random_volume_sv =0;

                /*
                    void setStreamVolume (int streamType, int index, int flags)
                        Sets the volume index for a particular stream.

                        This method has no effect if the device implements a fixed volume policy
                        as indicated by isVolumeFixed().

                        From N onward, volume adjustments that would toggle Do Not Disturb are not
                        allowed unless the app has been granted Do Not Disturb Access.
                        See isNotificationPolicyAccessGranted().

                    Parameters
                        streamType int : The stream whose volume index should be set.
                        index int : The volume index to set. See getStreamMaxVolume(int) for the largest valid value.


                        flags int : One or more flags.
                */
                /*
                    int FLAG_SHOW_UI
                        Show a toast containing the current volume.
                */
                // Set media volume level
                mAudioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC, // Stream type
                        random_volume_sv, // Index
                        AudioManager.FLAG_SHOW_UI // Flags
                );

                // Display the media volume info on text view
                mTVStats.setText("Media Current Volume : " + media_current_volume);
                mTVStats.append("\nMedia Max Volume : " + media_max_volume);
                mTVStats.append("\nMedia New Volume : " + random_volume_sv);
            }
        });
    }
}