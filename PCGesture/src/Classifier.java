import com.fastdtw.dtw.FastDTW;
import com.fastdtw.dtw.TimeWarpInfo;
import com.fastdtw.timeseries.TimeSeries;
import com.fastdtw.util.DistanceFunction;
import com.fastdtw.util.DistanceFunctionFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Classifier {

    private static final Classifier INSTANCE = new Classifier();

    private static final String DATA_DIRECTORY = "data";

    private static final int DTW_SEARCH_RADIUS = 10;

    private static final DistanceFunction DISTANCE_FUNCTION = DistanceFunctionFactory.getDistFnByName("EuclideanDistance");

    private ExecutorService executorService = Executors.newFixedThreadPool(3);

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

    public Gesture classify(TimeSeries timeSeries) throws Exception {
        TimeWarpInfo result = null;
        Gesture gesture = null;
        List<Future<TimeWarpInfo>> futures = new ArrayList<>();

        for (Gesture g : gestures) {
            futures.add(executorService.submit(new TestGesture(g, timeSeries)));
        }

        for (int i = 0; i < gestures.size(); ++i) {
            TimeWarpInfo info = futures.get(i).get();
            if (result == null || info.getDistance() < result.getDistance()) {
                result = info;
                gesture = gestures.get(i);
            }
        }

        System.out.println(gesture);
//        System.out.println(result);

        return gesture;
    }

    private class TestGesture implements Callable<TimeWarpInfo> {

        private Gesture gesture;

        private TimeSeries timeSeries;

        public TestGesture(Gesture gesture, TimeSeries timeSeries) {
            this.gesture = gesture;
            this.timeSeries = timeSeries;
        }

        @Override
        public TimeWarpInfo call() throws Exception {
            TimeWarpInfo result = null;
            for (TimeSeries base : gesture.getTimeSeries()) {
                TimeWarpInfo info = FastDTW.getWarpInfoBetween(base, timeSeries, DTW_SEARCH_RADIUS, DISTANCE_FUNCTION);
                if (result == null || info.getDistance() < result.getDistance()) {
                    result = info;
                }
            }
            return result;
        }
    }
}
