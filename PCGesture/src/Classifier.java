import com.fastdtw.dtw.FastDTW;
import com.fastdtw.dtw.TimeWarpInfo;
import com.fastdtw.timeseries.TimeSeries;
import com.fastdtw.util.DistanceFunction;
import com.fastdtw.util.DistanceFunctionFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Classifier {

    private static final Classifier INSTANCE = new Classifier();

    private static final String DATA_DIRECTORY = "data";

    private static final int DTW_SEARCH_RADIUS = 10;

    private static final DistanceFunction DISTANCE_FUNCTION = DistanceFunctionFactory.getDistFnByName("EuclideanDistance");

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    private List<Gesture> gestures = new ArrayList<>();

    private File data = new File(DATA_DIRECTORY);

    private Classifier() {
    }

    public void init() throws Exception {
        for (File file : data.listFiles()) {
            if (file.isFile() && !file.isHidden()) {
                addGesture(file);
            }
        }
    }

    public static Classifier getInstance() {
        return INSTANCE;
    }

    private void addGesture(File file) throws Exception {
        System.out.println("Adding " + file);

        Gesture gesture = new Gesture(file.getName());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<TimeSeries> timeSeriesList = gesture.getTimeSeries();
        String line;
        while ((line = reader.readLine()) != null) {
            timeSeriesList.add(Utils.dataToTimeSeries(line));
        }
        gestures.add(gesture);
    }

    public Gesture knn(int k, TimeSeries timeSeries) throws Exception {
        PriorityQueue<TimeWarpInfo> minHeap = new PriorityQueue<>(k, new TimeWarpDistanceComparator());
        Map<Future<TimeWarpInfo>, Gesture> futureMap = new HashMap<>();
        Map<TimeWarpInfo, Gesture> gestureMap = new HashMap<>();

        for (Gesture g : gestures) {
            for (TimeSeries base : g.getTimeSeries()) {
                Future<TimeWarpInfo> f = executorService.submit(new TestGesture(base, timeSeries));
                futureMap.put(f, g);
            }
        }

        for (Future<TimeWarpInfo> f : futureMap.keySet()) {
            TimeWarpInfo info = f.get();
            minHeap.add(info);
            gestureMap.put(info, futureMap.get(f));
        }

        // Count
        Map<Gesture, Integer> countMap = new HashMap<>();
        for (int i = 0; i < k; ++i) {
            Gesture g = gestureMap.get(minHeap.poll());
            if (!countMap.containsKey(g)) {
                countMap.put(g, 0);
            }
            int currCount = countMap.get(g);
            countMap.put(g, currCount + 1);
        }

        // Find max
        Gesture result = null;
        int max = Integer.MIN_VALUE;
        for (Gesture g : countMap.keySet()) {
            int count = countMap.get(g);
            if (count > max) {
                max = count;
                result = g;
            }
        }

        return result;
    }

    private class TestGesture implements Callable<TimeWarpInfo> {

        private TimeSeries t1;

        private TimeSeries t2;

        public TestGesture(TimeSeries t1, TimeSeries t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        @Override
        public TimeWarpInfo call() throws Exception {
            return FastDTW.getWarpInfoBetween(t1, t2, DTW_SEARCH_RADIUS, DISTANCE_FUNCTION);
        }
    }

    private class TimeWarpDistanceComparator implements Comparator<TimeWarpInfo> {

        @Override
        public int compare(TimeWarpInfo first, TimeWarpInfo second) {
            return (int) (first.getDistance() - second.getDistance());
        }
    }
}
