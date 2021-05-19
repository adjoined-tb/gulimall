package me.adjoined.gulimall.leetcode.recentcounter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class RecentCounter {

    Deque<Integer> requests = new ArrayDeque<>();
    public RecentCounter() {

    }

    public int pint(int t) {
        requests.addLast(t);

        while (true) {
            int r = requests.peekFirst();
            if (r < t - 3000) {
                requests.removeFirst();
            } else {
                break;
            }
        }


        return requests.size();
    }

    public int numDifferentIntegers(String word) {
        String[] arr = word.replaceAll("[a-zA-Z]", " ").split("\\s+");
        Set<String> differentInts = new HashSet<>();

        for (int i = 0; i < arr.length; i++) {
            String s = arr[i].replaceAll("^0*", "");
            if (s.equals("")) continue;
            differentInts.add(s);
        }
        return differentInts.size();
    }

    public static void main(String[] args) {
        RecentCounter rc = new RecentCounter();
        System.out.println(
        rc.numDifferentIntegers("a123bc34d8ef34a"));
    }
}
