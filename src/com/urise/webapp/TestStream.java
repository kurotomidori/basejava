package com.urise.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class TestStream {

    public static void main(String[] args) {
        Random random = new Random();
        int[] array = random.ints(5, 0, 10).toArray();
        List<Integer> list = Arrays.stream(array).boxed().collect(Collectors.toList());
        System.out.println(Arrays.toString(array));
        System.out.println(oddOrEven(list));
        System.out.println(minValue(array));
    }

    private static List<Integer> oddOrEven(List<Integer> list) {
        return new ArrayList<>(list.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0, Collectors.toList())).get(list.stream().mapToInt(Integer::valueOf).sum() % 2 != 0));
    }

    public static int minValue(int[] array) {
        int[] minArray = Arrays.stream(array).distinct().sorted().toArray();
        int minValue = 0;
        for (int i = 0; i < minArray.length; i++) {
            minValue += minArray[i] *  (int) Math.pow(10, minArray.length - (double) i - 1);
        }
        return minValue;
    }
}
