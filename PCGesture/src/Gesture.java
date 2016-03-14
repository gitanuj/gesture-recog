import com.fastdtw.timeseries.TimeSeries;

import java.util.ArrayList;
import java.util.List;

public class Gesture {

    private String name;

    private List<TimeSeries> timeSeries = new ArrayList<>();

    public Gesture(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<TimeSeries> getTimeSeries() {
        return timeSeries;
    }

    @Override
    public String toString() {
        return name;
    }
}
