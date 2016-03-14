import com.fastdtw.timeseries.TimeSeries;
import com.fastdtw.timeseries.TimeSeriesPoint;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Utils {

    public static void startThreadWithName(Runnable runnable, String name) {
        Thread thread = new Thread(runnable);
        thread.setName(name);
        thread.start();
    }

    public static String readFully(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(br);
        }
        return sb.toString();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    public static TimeSeries dataToTimeSeries(String data) {
        int count = 0;
        TimeSeries timeSeries = new TimeSeries(6);
        StringTokenizer tokenizer = new StringTokenizer(data, " ,");
        while (tokenizer.hasMoreTokens()) {
            double[] values = new double[6];
            for (int i = 0; i < 6; ++i) {
                values[i] = Double.parseDouble(tokenizer.nextToken());
            }
            timeSeries.addLast(count, new TimeSeriesPoint(values));
            count++;
        }
        return timeSeries;
    }
}