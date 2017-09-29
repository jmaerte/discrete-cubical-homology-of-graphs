package com.jmaerte.util;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Julian on 29/09/2017.
 */
public class Utils {

    public static int[] intersect(int[] a, int[] b) {
        Hashtable<Integer, Integer> H = new Hashtable<>();
        for(int i : a) H.put(i, i);
        ArrayList<Integer> list = new ArrayList<>();
        for(int i : b) {
            if(H.contains(i)) list.add(i);
        }
        Object[] arr = list.toArray();
        int[] res = new int[arr.length];
        for(int i = 0; i < arr.length; i++) res[i] = (int) arr[i];
        return res;
    }

}
