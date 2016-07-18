package com.kbajpai.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Numbers {
    public static List<Integer> randList(int len, int low, int high) {
        List<Integer> list = new ArrayList<>(len);

        Random r = new Random();
        for (int i = 0; i < len; i++) {
            list.add(r.nextInt(high - low) + low);
        }

        return list;
    }

    public static long getRand(int low, int high) {
        return new Random().nextInt(high - low) + low;
    }
}
