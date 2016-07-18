package com.kbajpai.threads;

import com.kbajpai.utils.Numbers;

import java.util.List;

public class FirstThread extends Thread {
    private final int LOW_VAL = 10000;
    private final int HIGH_VAL = 99999;
    private final int TOTAL_RAND;

    private final String mThreadName;
    private final Thread mCurrThread;
    private boolean mStarted;
    private final SecondThread mSecondThread;

    private boolean mWait = false;
    private boolean mExiting = false;

    public FirstThread(int loopSize) {
        TOTAL_RAND = loopSize;
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

    void setWait(boolean wait) {
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
        int j = 0;
        for (int i = LOW_VAL; i < HIGH_VAL; i++) {
            synchronized (this) {
                while (mWait) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (i + 1 >= HIGH_VAL) {
                    mExiting = true;
                    notify();
                }
                if (randStops.contains(i)) {
                    printResults(i, ++j);
                    mWait = true;
                    notify();
                }
            }
        }
        while (true) {
            synchronized (mSecondThread) {
                System.out.println("Exiting: " + mThreadName);
                if (mSecondThread.isExited()) {
                    break;
                }
                mSecondThread.notify();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("EXITED: " + mThreadName);
    }

    private void printResults(int i, int j) {
        System.out.format("%02d: ", j);
        try {
            Thread.sleep(Numbers.getRand(0, 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.format("%05d", i);
    }

    boolean isExiting() {
        return mExiting;
    }
}
