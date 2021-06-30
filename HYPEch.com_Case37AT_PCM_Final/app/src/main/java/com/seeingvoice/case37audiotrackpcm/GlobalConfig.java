package com.seeingvoice.case37audiotrackpcm;


public class GlobalConfig {

    /**
     * 采样率，现在能够保证在所有设备上使用的采样率是44100Hz, 但是其他的采样率（22050, 16000, 11025）在一些设备上也可以使用。
     */
    public static final int SAMPLE_RATE = 44100;
    public static final int AMPLITUDE = 65535;
    public static final double PI2 = 6.2831853;
}
