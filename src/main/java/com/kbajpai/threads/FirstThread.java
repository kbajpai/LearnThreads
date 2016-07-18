package com.kbajpai.threads;

import com.kbajpai.utils.Numbers;

import java.util.List;

public class FirstThread extends Thread {
    private final int LOW_VAL = 1;
    private final int HIGH_VAL = 100;
    private final int TOTAL_RAND = 50;

    private final String mThreadName;
    private final Thread mCurrThread;
    private boolean mStarted;
    private final SecondThread mSecondThread;

    private boolean mWait = false;
    private boolean mExiting = false;

    public FirstThread() {
        mThreadName = "FIRST_THREAD";
        mCurrThread = new Thread(this, mThreadName);
        mSecondThread = new SecondThread(this);
        start();
    }

    boolean hasStarted() {
        return mStarted;
    }

    boolean isWaiting() {
        return mWait;
    }

    public void setWait(boolean wait) {
        mWait = wait;
    }

    @Override
    public void run() {
        synchronized (this) {
            mStarted = true;
            notify();
        }
        System.out.println("STARTING: " + mThreadName);

        List<Integer> randStops = Numbers.randList(TOTAL_RAND, LOW_VAL, HIGH_VAL);
        for (int i = LOW_VAL; i < HIGH_VAL; i++) {
            synchronized (this) {
                if (i + 1 >= HIGH_VAL) {
                    mExiting = true;
                    notify();
                }
                if (randStops.contains(i)) {
                    printResults(i);
                    mWait = true;
                    notify();
                }
            }
            while (mWait) {
                synchronized (mSecondThread) {
                    if (mSecondThread.isWait()) {
                        break;
                    }
                }
            }
        }
        try {
            synchronized (this) {
                while (!mSecondThread.isExited()) {
                    wait(250);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Exiting: " + mThreadName);
    }

    private void printResults(int i) {
        System.out.format("FIRST: %05d", i);
    }

    boolean isExiting() {
        return mExiting;
    }
}
