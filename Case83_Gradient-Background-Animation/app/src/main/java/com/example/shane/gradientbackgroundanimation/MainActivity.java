package com.example.shane.gradientbackgroundanimation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = (ImageView) findViewById(R.id.background);

        Drawable backgrounds[] = new Drawable[5];
        backgrounds[0] = ContextCompat.getDrawable(this, R.drawable.g31);
        backgrounds[1] = ContextCompat.getDrawable(this, R.drawable.g32);
        backgrounds[2] = ContextCompat.getDrawable(this, R.drawable.g33);
        backgrounds[3] = ContextCompat.getDrawable(this, R.drawable.g34);
        backgrounds[4] = ContextCompat.getDrawable(this, R.drawable.g35);

        Crossfade(image, backgrounds, 2500);
    }

    public void Crossfade(final ImageView image, final Drawable layers[], final int speedInMs) {
        class BackgroundGradientThread implements Runnable {
            Context mainContext;
            TransitionDrawable crossFader;
            boolean first = true;

            BackgroundGradientThread(Context c) {
                mainContext = c;
            }

            public void run() {
                Handler mHandler = new Handler(mainContext.getMainLooper());
                boolean reverse = false;

                while (true) {
                    if (!reverse) {
                        for (int i = 0; i < layers.length - 1; i++) {
                            Drawable tLayers[] = new Drawable[2];
                            tLayers[0] = layers[i];
                            tLayers[1] = layers[i + 1];

                            final TransitionDrawable tCrossFader = new TransitionDrawable(tLayers);
                            tCrossFader.setCrossFadeEnabled(true);

                            Runnable transitionRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    image.setImageDrawable(tCrossFader);
                                    tCrossFader.startTransition(speedInMs);
                                }
                            };

                            mHandler.post(transitionRunnable);

                            try {
                                Thread.sleep(speedInMs);
                            } catch (Exception e) {
                            }
                        }

                        reverse = true;
                    }
                    else if (reverse) {
                        for (int i = layers.length - 1; i > 0; i--) {
                            Drawable tLayers[] = new Drawable[2];
                            tLayers[0] = layers[i];
                            tLayers[1] = layers[i - 1];

                            final TransitionDrawable tCrossFader = new TransitionDrawable(tLayers);
                            tCrossFader.setCrossFadeEnabled(true);

                            Runnable transitionRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    image.setImageDrawable(tCrossFader);
                                    tCrossFader.startTransition(speedInMs);
                                }
                            };

                            mHandler.post(transitionRunnable);

                            try {
                                Thread.sleep(speedInMs);
                            } catch (Exception e) {
                            }
                        }

                        reverse = false;
                    }
                }
            }
        }

        Thread backgroundThread = new Thread(new BackgroundGradientThread(this));
        backgroundThread.start();
    }
}
