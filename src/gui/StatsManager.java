package gui;

import com.sun.jmx.remote.internal.ArrayQueue;
import sun.awt.image.ImageWatched;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;

public class StatsManager {
    private LinkedList<Stats> statsQueue;

    public StatsManager() {
        this.statsQueue = new LinkedList<Stats>();
    }

    public void addStats(Stats statsEntry) {
        // newest elements added to the front
        statsQueue.addFirst(statsEntry);
    }

    public LinkedList<Stats> getMostRecentStats() {
        // Returns a LinkedList of the most recent results, starting from the most recent one
        int numberOfStats = 5;
        int statsToGet = Math.min(numberOfStats, statsQueue.size());
        LinkedList<Stats> results = new LinkedList<>();
        for (int i=0; i<statsToGet; i++) {
            results.addLast(statsQueue.get(i));
        }
        return results;
    }
}
