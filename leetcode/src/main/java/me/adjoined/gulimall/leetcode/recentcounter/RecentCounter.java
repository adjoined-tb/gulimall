package me.adjoined.gulimall.leetcode.recentcounter;

import javax.xml.bind.SchemaOutputResolver;
import java.util.*;

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

    public double new21Game(int n, int k, int maxPts) {
        if (k > n) return 0.0;
        if (k == 0 || n == 0) return 1.0;

        double[] prob = new double[n+2];
        for (int i = 0; i <= n - k + 1; i++) {
            prob[n+1-i] = i;
        }

        for (int x = k - 1; x >= 0; x--) {
            int right = Math.min(n+1, x + maxPts + 1);
            prob[x] = prob[x+1] + (prob[x+1] - prob[right]) / maxPts;
        }
        return prob[0] - prob[1];
    }

    public static void main(String[] args) {
        RecentCounter rc = new RecentCounter();
        System.out.println(
        rc.numDifferentIntegers("a123bc34d8ef34a"));

        System.out.println(rc.new21Game(10, 1, 10));

        List<Object> l = new ArrayList<>();
        l.add(1);
        System.out.println(l.get(0));
    }
}
