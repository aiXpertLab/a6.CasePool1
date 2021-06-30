package com.hypech.case8thread.runnable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Define a button to start the thread
    Button button;

    // Step1: Create class MyThread1 to implement Runnable interface
    // two windows selling tickets in different speed: each/s and each/3seconds.

    // each/s thread
    private class MyThread1 implements Runnable{

        private int ticketQty = 100;

        @Override
        public void run(){
            while (ticketQty>0){
                ticketQty--;
                System.out.println(Thread.currentThread().getName() + "Sold 1 ticket, left: "+ticketQty);

                try {
                    Thread.sleep(1000);     //selling ticket speed is 1 piece/second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // each/3 seconds thread
    private class MyThread2 implements Runnable{

        // initial 100 tickets for each window
        private int ticketQty = 100;

        // Step2: rewrite run() method to do something
        @Override
        public void run(){
            while (ticketQty>0){
                ticketQty--;
                System.out.println(Thread.currentThread().getName() + "Sold 1 ticket, left: "+ticketQty);

                try {
                    // each/3seconds
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button click to start a new thread
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Step3: instance of the thread class
                // create 2 threads here for 2 windows.
                MyThread1 mt1 = new MyThread1();
                MyThread2 mt2 = new MyThread2();

                // Two threads for 2 windows
                Thread mt11 = new Thread(mt1, "Win11");
                Thread mt22 = new Thread(mt2, "Win22");

                //Step 4: start the thread with start()
                //start 2 threads for win1 and win1.
                //Two windows selling tickets same time.
                mt11.start();
                mt22.start();

            }
        });
    }
}