import com.fastdtw.dtw.FastDTW;
import com.fastdtw.dtw.TimeWarpInfo;
import com.fastdtw.timeseries.TimeSeries;
import com.fastdtw.util.DistanceFunction;
import com.fastdtw.util.DistanceFunctionFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Classifier {

    private static final Classifier INSTANCE = new Classifier();

    private static final String DATA_DIRECTORY = "data";

    private static final String CONFIG_FILE = "config-file";

    private static final int DTW_SEARCH_RADIUS = 10;

    private static final DistanceFunction DISTANCE_FUNCTION = DistanceFunctionFactory.getDistFnByName("EuclideanDistance");

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    private Map<String, String> gestureCommandMap = new HashMap<>();

    private List<Gesture> gestures = new ArrayList<>();

    private File data = new File(DATA_DIRECTORY);

    private Classifier() {
    }

    public void init() throws Exception {
        Map map = (Map) Utils.readObjectFromFile(new File(DATA_DIRECTORY, CONFIG_FILE));
        if (map != null) {
            gestureCommandMap = map;
        }

        data.mkdirs();

        for (File file : data.listFiles()) {
            if (file.isFile() && !file.isHidden() && !file.getName().equals(CONFIG_FILE)) {
                String name = file.getName();
                addGestureInMem(name, gestureCommandMap.get(name), new BufferedReader(new InputStreamReader(new FileInputStream(file))));
            }
        }
    }

    public static Classifier getInstance() {
        return INSTANCE;
    }

    private void addGestureInMem(String name, String command, BufferedReader reader) throws Exception {
        System.out.println("Adding in mem:  " + name);

        Gesture gesture = new Gesture(name);
        gesture.setCommand(command);
        String line;
        while ((line = reader.readLine()) != null) {
            gesture.getTimeSeries().add(Utils.dataToTimeSeries(line));
        }
        gestures.add(gesture);
    }

    public void addGesture(String name, String command, String values) throws Exception {
        System.out.println("Storing on disk: " + name);

        // Write values to file
        File file = new File(DATA_DIRECTORY, name);
        FileWriter writer = new FileWriter(file, false);
        writer.write(values);
        Utils.closeQuietly(writer);

        // Add command to config
        gestureCommandMap.put(name, command);
        Utils.writeObjectToFile(new File(DATA_DIRECTORY, CONFIG_FILE), gestureCommandMap);

        // Add gesture to in-memory structure
        addGestureInMem(name, command, new BufferedReader(new StringReader(values)));
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
