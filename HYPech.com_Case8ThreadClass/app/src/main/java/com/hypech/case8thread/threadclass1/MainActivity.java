package com.hypech.case8thread.threadclass1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

// Step1: Create class MyThread extended from Android's Thread

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Step3: instance of the thread class
                // create 2 threads here for 2 windows.
                MyThread mt1 = new MyThread("Win 1");
                MyThread mt2 = new MyThread("Win 2");

                //Step 4: start the thread with start()
                //start 2 threads for win1 and win1. Two windows selling tickets same time.
                mt1.start();
                mt2.start();
            }
        });
    }
}

