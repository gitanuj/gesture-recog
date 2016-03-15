import com.fastdtw.timeseries.TimeSeries;

import java.util.ArrayList;
import java.util.List;

public class Gesture {

    private String name;

    private String command;

    private List<TimeSeries> timeSeries = new ArrayList<TimeSeries>();

    public Gesture(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public List<TimeSeries> getTimeSeries() {
        return timeSeries;
    }

    @Override
    public String toString() {
        return name;
    }
}
