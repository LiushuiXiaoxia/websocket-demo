package cn.mycommons.client;

import java.util.Arrays;
import java.util.Random;

public class BubbleSortTest {

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
        for (int i = 0; i < list.length - 1; i++) {
            for (int j = 0; j < list.length - 1 - i; j++) {
                if (list[j] > list[j + 1]) {
                    int t = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = t;
                }
                System.out.println(Arrays.toString(list));
            }
            // System.out.println(Arrays.toString(list));
            System.out.println();
        }
    }

    private static void sort2(int[] list) {
        for (int i = 0; i < list.length; i++) {
            for (int j = i + 1; j < list.length; j++) {
                if (list[i] > list[j]) {
                    int t = list[i];
                    list[i] = list[j];
                    list[j] = t;
                }
            }
            System.out.println(Arrays.toString(list));
        }
    }
}