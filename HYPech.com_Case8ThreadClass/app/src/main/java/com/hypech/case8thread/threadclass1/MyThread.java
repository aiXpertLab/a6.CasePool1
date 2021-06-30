package com.hypech.case8thread.threadclass1;

public class MyThread extends Thread{
    // Step1: Create class MyThread extended from Android's Thread

    private int ticketQty = 100;   // initial 100 tickets for each window
    private final String winName;        // window's name i.e. thread's name

    public MyThread(String pName){
        this.winName = pName;
    }

    // Step2: rewrite run() method to do something
    @Override
    public void run(){
        while (ticketQty>0){
            ticketQty--;
            System.out.println(winName + "Sold 1 ticket, left: "+ticketQty);

            try {
                Thread.sleep(1000);     //selling ticket speed is 1 piece/second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
