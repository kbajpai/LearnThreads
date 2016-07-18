package com.kbajpai;

import com.kbajpai.threads.FirstThread;

public class Main {

    public static void main(String[] args) {
        int loopSize = 50;

        if (args.length > 0 && null != args[0]) {
            loopSize = Integer.parseInt(args[0]);
        }
        new FirstThread(loopSize);
    }
}
