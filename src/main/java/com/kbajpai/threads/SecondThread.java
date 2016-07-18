package com.kbajpai.threads;

import com.kbajpai.utils.Numbers;

class SecondThread extends Thread {
    private final FirstThread mFirstThread;

    private final String mThreadName;
    private final Thread mCurrentThread;
    private final int MIN_DOTS = 3;
    private final int MAX_DOTS = 10;
    private boolean mStop = false;
    private boolean mWait = true;

    boolean isExited() {
        return mExiting;
    }

    private boolean mExiting;

    public void Stop() {
        mStop = true;
    }

    boolean isWait() {
        return mWait;
    }

    void setWait(boolean wait) {
        mWait = wait;
    }


    SecondThread(FirstThread firstThread) {
        mFirstThread = firstThread;
        mThreadName = "SECOND_THREAD";
        mCurrentThread = new Thread(this, mThreadName);
        start();
    }

    @Override
    public void run() {
        System.out.println("ENTERING: " + mThreadName);

        while (true) {
            if (mFirstThread.hasStarted()) {
                synchronized (this) {
                    mWait = true;
                    notify();
                }
                if (mFirstThread.isWaiting()) {
                    break;
                }
            }
        }

        while (true) {
            synchronized (mFirstThread) {
                if (mFirstThread.isExiting()) {
                    synchronized (this) {
                        mExiting = true;
                        notify();
                    }
                    break;
                }

                if (mFirstThread.isWaiting()) {
                    try {
                        loading();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mFirstThread.setWait(false);
                    mFirstThread.notify();
                }
                while (!mFirstThread.isWaiting()) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        synchronized (mFirstThread) {
            mExiting = true;
        }
        System.out.println("Exiting: " + mThreadName);
    }

    private void loading() throws InterruptedException {
        for (int i = 0; i < Numbers.getRand(MIN_DOTS, MAX_DOTS); i++) {
            System.out.print(".");
            Thread.sleep(250);
        }
        System.out.println();
    }
}