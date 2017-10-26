package com.acxiom.seals.web.scrape.script.common.utils;

import java.util.concurrent.TimeUnit;

public class SleepUtil {

    private final static long SHORT_SLEEP_TIME_ARRAY[] = {2, 4, 6, 8, 10, 12};
    private final static long LONG_SLEEP_TIME_ARRAY[] = {30, 40, 50, 60, 70, 80};

    public static void longSleep() {
        int random = (int)(Math.random() * 100) % LONG_SLEEP_TIME_ARRAY.length;
        try {
            TimeUnit.SECONDS.sleep(LONG_SLEEP_TIME_ARRAY[random]);
        } catch (InterruptedException e) {

        }
    }

    public static void shortSleep() {
        int random = (int)(Math.random() * 100) % SHORT_SLEEP_TIME_ARRAY.length;
        try {
            TimeUnit.SECONDS.sleep(SHORT_SLEEP_TIME_ARRAY[random]);
        } catch (InterruptedException e) {

        }
    }

    public static void sleep(int seconds){
        if(seconds>0){
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
