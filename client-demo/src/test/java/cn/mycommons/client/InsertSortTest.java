package cn.mycommons.client;

import java.util.Arrays;
import java.util.Random;

public class InsertSortTest {

    private static final int LEN = 10;

    public static void main(String[] args) {
        int[] list = new int[LEN];
        Random random = new Random();
        for (int i = 0; i < LEN; i++) {
            list[i] = random.nextInt(LEN * 10);
        }

        System.out.println("before " + Arrays.toString(list));
        long time = System.currentTimeMillis();

        sort(list);

        System.out.println("after " + Arrays.toString(list));

        System.out.println(System.currentTimeMillis() - time);
    }

    private static void sort(int[] list) {
        for (int i = 0; i < list.length; i++) {
            int min = list[i];
            int k = i + 1;
            for (int j = i + 1; j < list.length; j++) {
                if (min < list[j]) {
                    min = list[j];
                    k = j;
                }
            }
            if (min != list[i]) {
                list[k] = list[i];
                list[i] = min;
            }
            System.out.println(Arrays.toString(list));
        }
    }
}